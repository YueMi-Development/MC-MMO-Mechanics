package org.yuemi.mmomechanics.plugin.skill.trigger;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import io.papermc.paper.event.player.PlayerTradeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import com.destroystokyo.paper.event.entity.CreeperIgniteEvent;
import org.yuemi.libs.api.event.EventApi;
import org.yuemi.mmomechanics.api.skill.target.EntityTarget;
import org.yuemi.mmomechanics.api.skill.target.LocationTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Dynamically registers Bukkit/Paper event listeners using YueMiLibs EventApi.
 */
public final class TriggerRegistration {

    private final JavaPlugin plugin;
    private final TriggerManager triggerManager;
    private final TimerManager timerManager;

    // Track entities that freshly spawned to distinguish spawn vs chunk load
    private final Set<Entity> freshlySpawned = Collections.newSetFromMap(new WeakHashMap<>());

    public TriggerRegistration(@NotNull JavaPlugin plugin, @NotNull TriggerManager triggerManager, @NotNull TimerManager timerManager) {
        this.plugin = plugin;
        this.triggerManager = triggerManager;
        this.timerManager = timerManager;
    }

    public void register() {
        EventApi eventApi = Bukkit.getServicesManager().load(EventApi.class);
        if (eventApi == null) {
            plugin.getLogger().warning("YueMiLibs EventApi service not found. Triggers will not be registered!");
            return;
        }

        plugin.getLogger().info("Registering MMO Mechanics event triggers using YueMiLibs EventApi...");

        // ==========================================
        // 1. COMBAT / DAMAGE TRIGGERS
        // ==========================================

        // onAttack & onDamaged (Entity vs Entity)
        eventApi.bukkit().subscribe(EntityDamageByEntityEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    Entity victim = event.getEntity();
                    Entity damager = event.getDamager();
                    if (damager instanceof Projectile proj && proj.getShooter() instanceof Entity shooter) {
                        damager = shooter;
                    }
                    
                    Target victimTarget = new EntityTarget(victim);
                    Target damagerTarget = new EntityTarget(damager);

                    triggerManager.executeTrigger(damager, "onAttack", victimTarget);
                    triggerManager.executeTrigger(victim, "onDamaged", damagerTarget);
                });

        // onDamaged (Environmental / General Damage)
        eventApi.bukkit().subscribe(EntityDamageEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    if (event instanceof EntityDamageByEntityEvent) return; // Handled above
                    triggerManager.executeTrigger(event.getEntity(), "onDamaged", null);
                });

        // onDeath & onPlayerKill
        eventApi.bukkit().subscribe(EntityDeathEvent.class)
                .priority(EventPriority.MONITOR)
                .handler(event -> {
                    LivingEntity dead = event.getEntity();
                    Player killer = dead.getKiller();
                    Target killerTarget = killer != null ? new EntityTarget(killer) : null;

                    triggerManager.executeTrigger(dead, "onDeath", killerTarget);

                    if (killer != null) {
                        triggerManager.executeTrigger(killer, "onPlayerKill", new EntityTarget(dead));
                    }
                });

        // onChangeTarget
        eventApi.bukkit().subscribe(EntityTargetLivingEntityEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    LivingEntity target = event.getTarget();
                    Target targetTarget = target != null ? new EntityTarget(target) : null;
                    triggerManager.executeTrigger(event.getEntity(), "onChangeTarget", targetTarget);
                });

        // ==========================================
        // 2. LIFECYCLE / SPAWN TRIGGERS
        // ==========================================

        // onSpawn, onReady, onSpawnOrLoad (Creatures)
        eventApi.bukkit().subscribe(CreatureSpawnEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    LivingEntity entity = event.getEntity();
                    freshlySpawned.add(entity);
                    timerManager.trackEntity(entity);

                    triggerManager.executeTrigger(entity, "onSpawn", null);
                    triggerManager.executeTrigger(entity, "onSpawnOrLoad", null);

                    if (event.getSpawnReason() == SpawnReason.SPAWNER) {
                        triggerManager.executeTrigger(entity, "onReady", null);
                    }
                });

        // onSpawn / onSpawnOrLoad (Other entities, e.g. ArmorStands)
        eventApi.bukkit().subscribe(EntityPlaceEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    Entity entity = event.getEntity();
                    freshlySpawned.add(entity);
                    timerManager.trackEntity(entity);

                    triggerManager.executeTrigger(entity, "onSpawn", null);
                    triggerManager.executeTrigger(entity, "onSpawnOrLoad", null);
                });

        // onLoad & onSpawnOrLoad (Entity Load from Chunk/Restart)
        eventApi.bukkit().subscribe(EntityAddToWorldEvent.class)
                .priority(EventPriority.MONITOR)
                .handler(event -> {
                    Entity entity = event.getEntity();
                    timerManager.trackEntity(entity);
                    
                    // If it was not freshly spawned, it is loaded
                    if (!freshlySpawned.contains(entity)) {
                        triggerManager.executeTrigger(entity, "onLoad", null);
                        triggerManager.executeTrigger(entity, "onSpawnOrLoad", null);
                    }
                });

        // onDespawn
        eventApi.bukkit().subscribe(EntityRemoveFromWorldEvent.class)
                .priority(EventPriority.MONITOR)
                .handler(event -> {
                    Entity entity = event.getEntity();
                    triggerManager.executeTrigger(entity, "onDespawn", null);
                    freshlySpawned.remove(entity);
                });

        // ==========================================
        // 3. INTERACTION / ACTION TRIGGERS
        // ==========================================

        // onInteract
        eventApi.bukkit().subscribe(PlayerInteractEntityEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    Entity clicked = event.getRightClicked();
                    Player player = event.getPlayer();
                    triggerManager.executeTrigger(clicked, "onInteract", new EntityTarget(player));
                });

        // onTame
        eventApi.bukkit().subscribe(EntityTameEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    Entity pet = event.getEntity();
                    Entity owner = (Entity) event.getOwner();
                    triggerManager.executeTrigger(pet, "onTame", new EntityTarget(owner));
                });

        // onBreed
        eventApi.bukkit().subscribe(EntityBreedEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    LivingEntity parent = event.getEntity();
                    LivingEntity other = event.getFather();
                    triggerManager.executeTrigger(parent, "onBreed", new EntityTarget(other));
                });

        // onDismounted
        eventApi.bukkit().subscribe(EntityDismountEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    Entity vehicle = event.getDismounted();
                    Entity passenger = event.getEntity();
                    triggerManager.executeTrigger(vehicle, "onDismounted", new EntityTarget(passenger));
                });

        // onBucket
        eventApi.bukkit().subscribe(PlayerBucketEntityEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    Entity entity = event.getEntity();
                    Player player = event.getPlayer();
                    triggerManager.executeTrigger(entity, "onBucket", new EntityTarget(player));
                });

        // onTrade
        eventApi.bukkit().subscribe(PlayerTradeEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    Entity villager = event.getVillager();
                    Player player = event.getPlayer();
                    triggerManager.executeTrigger(villager, "onTrade", new EntityTarget(player));
                });

        // onChangeWorld (Players)
        eventApi.bukkit().subscribe(PlayerChangedWorldEvent.class)
                .priority(EventPriority.MONITOR)
                .handler(event -> {
                    triggerManager.executeTrigger(event.getPlayer(), "onChangeWorld", null);
                });

        // onChangeWorld (Entities via Teleport)
        eventApi.bukkit().subscribe(EntityTeleportEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    if (event.getFrom().getWorld() != event.getTo().getWorld()) {
                        triggerManager.executeTrigger(event.getEntity(), "onChangeWorld", null);
                    }
                });

        // onChangeWorld (Entities via Portals)
        eventApi.bukkit().subscribe(EntityPortalEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    if (event.getTo() != null && event.getFrom().getWorld() != event.getTo().getWorld()) {
                        triggerManager.executeTrigger(event.getEntity(), "onChangeWorld", null);
                    }
                });

        // ==========================================
        // 4. PROJECTILE TRIGGERS
        // ==========================================

        // onShoot
        eventApi.bukkit().subscribe(EntityShootBowEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    LivingEntity shooter = event.getEntity();
                    Entity projectile = event.getProjectile();
                    triggerManager.executeTrigger(shooter, "onShoot", new EntityTarget(projectile));
                });

        eventApi.bukkit().subscribe(ProjectileLaunchEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    Projectile proj = event.getEntity();
                    if (proj.getShooter() instanceof Entity shooter) {
                        triggerManager.executeTrigger(shooter, "onShoot", new EntityTarget(proj));
                    }
                });

        // onBowHit, onProjectileHit, onProjectileLand
        eventApi.bukkit().subscribe(ProjectileHitEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    Projectile proj = event.getEntity();
                    if (!(proj.getShooter() instanceof Entity shooter)) return;

                    Entity hitEntity = event.getHitEntity();
                    org.bukkit.block.Block hitBlock = event.getHitBlock();

                    if (hitEntity != null) {
                        Target hitTarget = new EntityTarget(hitEntity);
                        triggerManager.executeTrigger(shooter, "onProjectileHit", hitTarget);
                        
                        // If it's a bow arrow
                        if (proj instanceof org.bukkit.entity.Arrow) {
                            triggerManager.executeTrigger(shooter, "onBowHit", hitTarget);
                        }
                    }

                    if (hitBlock != null) {
                        Target blockTarget = new LocationTarget(hitBlock.getLocation());
                        triggerManager.executeTrigger(shooter, "onProjectileLand", blockTarget);
                    }
                });

        // ==========================================
        // 5. MOB EXPLOSION TRIGGERS
        // ==========================================

        // onPrime (Creeper starts fuse)
        eventApi.bukkit().subscribe(CreeperIgniteEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    triggerManager.executeTrigger(event.getEntity(), "onPrime", null);
                });

        // onExplode
        eventApi.bukkit().subscribe(EntityExplodeEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    triggerManager.executeTrigger(event.getEntity(), "onExplode", null);
                });

        // onCreeperCharge
        eventApi.bukkit().subscribe(CreeperPowerEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    triggerManager.executeTrigger(event.getEntity(), "onCreeperCharge", null);
                });

        // onTeleport
        eventApi.bukkit().subscribe(EntityTeleportEvent.class)
                .priority(EventPriority.MONITOR)
                .ignoreCancelled(true)
                .handler(event -> {
                    triggerManager.executeTrigger(event.getEntity(), "onTeleport", null);
                });
    }
}
