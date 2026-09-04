# MC-MMO-Mechanics

Skill system engine for PaperMC servers. Defines skills via JSON5 configs with triggers, targeters, mechanics, and conditions.

## Stack
- PaperMC 1.21+, Java 21, Gradle (Kotlin DSL)
- Soft-depends: YueMiLibs, MmoEssence

## Skill Structure (JSON5)
```json5
{
  name: "Fire Breath",
  triggers: ["onAttack"],
  mechanics: [
    {
      mechanic: "ignite{ticks=60}",
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

## Key Parsers
- `ConditionParser` - Parses condition types
- `MechanicParser` - Parses mechanic types (~40 types)
- `TargeterParser` - Parses targeter types (~60 types)

## Conventions
- Packages: `org.yuemi.mmomechanics.{api,plugin}`
- Parser classes in `plugin.skill.parser.{mechanic,condition,targeter}`
- Mechanic implementations in `plugin.skill.mechanic.{category}/`
