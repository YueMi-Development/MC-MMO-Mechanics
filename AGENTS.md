# MC-MMO-Mechanics

Skill system engine for PaperMC servers. Defines skills via JSON5 configs with triggers, targeters, mechanics, and conditions.

## Stack
- PaperMC 1.21+, Java 21, Gradle (Kotlin DSL)
- Soft-depends: YueMiLibs, MmoEssence (optional hook)

## Skill Configuration (JSON5)
```json5
{
  name: "Fire Breath",
  triggers: ["onAttack"],
  mechanics: [
    {
      mechanic: "damage{amount=10;element=FIRE}",
      targeter: "livingincone{r=8;a=45}",
      conditions: []
    }
  ]
}
```

## Building Blocks
| Block | Interface | Purpose |
|-------|-----------|---------|
| Trigger | `Trigger` | When skill fires (onAttack, onDamaged, onTimer, etc.) |
| Targeter | `Targeter` | Who receives effect (entitiesinradius, livingincone, etc.) |
| Mechanic | `Mechanic` | The action (damage, lightning, potion, particle, etc.) |
| Condition | `Condition` | Gate check (isday, isnight, etc.) |

## MmoEssence Integration
When `hooks.mmo-essence: true` in config:
- Damage mechanics can specify `element=FIRE` etc.
- `EssenceHook.getDamageMultiplier()` applies essence-based scaling
- Falls back to physical damage (1.0 multiplier) if not specified

## Key Methods
- `DamageMechanic.calculateDamage(baseDamage, caster)` - Future-proof damage calculation with essence scaling

## Conventions
- Packages: `org.yuemi.mmomechanics.{api,plugin}`
- Parser classes in `plugin.skill.parser.{mechanic,condition,targeter}`
- Mechanic implementations in `plugin.skill.mechanic.{category}/`

## Folia
- Skill triggers are dispatched to the caster's entity scheduler before target resolution.
- On Folia, `SkillExecutorImpl` dispatches each mechanic application to the target's owning region: entity targets via the entity scheduler, location targets via the region scheduler.
- Mechanic steps remain sequential; each step waits for all target tasks to complete before the next step begins.
- On Paper, mechanic execution stays inline to preserve existing tick timing and ordering.
- Mechanics that read the caster's position, direction, or base attack damage should use `SkillContext.getCasterSnapshot()` rather than reading the live caster from a target's region thread.
- Conditions run on the target's region, so a condition that reads the caster's world (such as `isday` / `isnight`) must use the snapshot too.
- Mechanics that mutate the caster while processing a foreign-region target must use `TargetDispatcher.dispatchToCaster(...)`.
- When mutating a second block/entity distinct from the target, dispatch that operation to the second object's owning region too.
