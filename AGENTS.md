# MMO Mechanics

Skill system engine for PaperMC servers. Defines skills with triggers, mechanics, targeters, and conditions via JSON5 configuration.

## Stack
- PaperMC 1.21+, Java 21, Gradle (Kotlin DSL), JSON5
- Dependencies: Paper API, YueMiLibs

## API
- `MmoMechanicsApiProvider.getApi()` - Main API
- Cast skills, send messages, parse placeholders

## Conventions
- Packages: `org.yuemi.mmomechanics.{api,plugin,plugin.skill}`
- Classes: PascalCase, `*Manager`, `*Parser`, `*Impl`
- Methods/vars: camelCase, CONSTANTS: UPPER_SNAKE_CASE
- Constants: only when enum not suitable

## Adding Features
- **Mechanics**: Interface in `api/skill/mechanic/`, impl in `plugin/skill/mechanic/`, parser in `plugin/skill/parser/mechanic/`, register in `MechanicParser`
- **Targeters**: Interface in `api/skill/targeter/`, impl in `plugin/skill/targeter/`, parser in `plugin/skill/parser/targeter/`, register in `TargeterParser`
- **Triggers**: Enum/listener in `plugin/skill/trigger/`, register in `TriggerManager`

## Best Practices
- Async heavy operations
- Validate config values, log errors clearly
- Paper API over Spigot

## Build
```
./gradlew build
```
