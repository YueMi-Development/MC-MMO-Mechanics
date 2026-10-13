# MMO Mechanics

A PaperMC plugin that runs an event-driven skill system: JSON5 skill files, a rich targeter suite, modular mechanics, time-of-day / accessory conditions, equip and unequip triggers, and optional MmoEssence elemental damage scaling.

<a href="https://modrinth.com/plugin/R8PzhBth" target="_blank" rel="noopener noreferrer"><img alt="modrinth" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg"></a>

![](https://img.shields.io/bstats/players/34673?color=green&label=Players)
![](https://img.shields.io/bstats/servers/34673?color=green&label=Servers)

---

## Features

### 1. JSON5 Skill Configuration
- **Comments, single quotes, unquoted field names, trailing commas** — write skills in human-friendly JSON5.
- **Top-level `triggers: [...]`** declares when the skill fires. Standard ids: `onAttack`, `onDamaged`, `onDeath`, `onKill`, `onTimer`, `onClick`, `onEquip`, `onUnequip`, `command`.
- **Per-mechanic `targeter:` and `conditions:`** for fine-grained control over which entities are hit and which circumstances gate the effect.

### 2. Rich Trigger System
- **Built-in Event Listeners** for combat, lifecycle, projectile, and player-interaction events — all subscribed through YueMiLibs `EventApi`.
- **EquipTriggerRegistration** listens to Paper's `PlayerArmorChangeEvent` (with a Spigot fallback) and fires `onEquip` / `onUnequip` on every armor-slot transition. The same triggers also fire from MC-MMO-Accessories when an item is placed in or removed from an accessory slot.
- **Public API**: `MmoMechanicsApi.fireTrigger(UUID, String)` lets other plugins dispatch trigger-based skill execution through `TriggerManager`. `castSkill(UUID, String)` is still available for direct manual casts.

### 3. MmoEssence Integration
When `hooks.mmo-essence: true` in `config.yml`, the `damage` mechanic honours an `element=` parameter (`FIRE`, `ICE`, `LIGHTNING`, …). `EssenceHook.getDamageMultiplier(player, element)` returns the per-element scaling.

### 4. PlaceholderAPI Hook
Placeholders are expanded in skill names, message mechanics, titles, action bars, and targeter expressions. Hooks through YueMiLibs' PlaceholderAPI integration.

---

> See `core-plugin/src/main/resources/skills/` for the full list of default skills, conditions, targeters and mechanics.

## Commands

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/mmomechanics cast <skill> [player]` | Cast a skill on the player (or as the player) | `mmomechanics.cast` |
| `/mmomechanics reload` | Reload all skill files | `mmomechanics.admin` |

Aliases: `/mmom`.
