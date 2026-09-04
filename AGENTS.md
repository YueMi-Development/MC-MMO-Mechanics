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
