# Wordle Clone (Java)

A desktop Wordle game built in Java with Swing, made for our OOP course project.

## Features
- 6 attempts to guess a 5-letter word, with green / yellow / grey tile feedback
- Correct handling of duplicate letters
- Dictionary validation (non-words are rejected)
- Normal and Hard mode (Hard forces you to reuse revealed hints)
- Restart button for a new random word
- Player statistics saved to a file (games played, win %, streak)

## How to run
Requires JDK 17 or later (developed and tested on JDK 17.0.15 and JDK 21.0.12.1)

```
javac -d bin src/*.java
java -cp bin GameLauncher
```
Run these from the project root so the word files in `data/` are found.

## How to play
1. Type a 5-letter word and press Enter.
2. Green = right letter, right spot. Yellow = right letter, wrong spot. Grey = not in the word.
3. You have 6 attempts.
4. Hard mode: any hint you've revealed must be used in later guesses.

## Project structure
```
src/   Java source files (one class per file)
data/  answers.txt, valid_words.txt
bin/   compiled classes (not tracked in Git)
```

## OOP concepts used
| Concept | Where |
|---|---|
| Abstraction | `Game` (abstract class), `Evaluator` (interface) |
| Inheritance | `NormalGame` and `HardGame` extend `Game` |
| Polymorphism / overriding | `validate()` behaves differently per mode; `Evaluator` can be swapped |
| Encapsulation | `Tile`, `Guess`, `PlayerStats` with private fields and getters |
| Enums | `LetterState` (CORRECT, PRESENT, ABSENT) |
| Custom exception | `InvalidWordException` |
| Collections | `List`, `Map`, `Set` for guesses, keyboard states, word list |
| File I/O | `WordRepository` (word lists), `PlayerStats` (saved stats) |
| Event handling | Swing listeners in `GameWindow` |

## Design notes
Game logic is separate from the UI: `Game` never imports Swing, so the same logic runs from the console or the GUI. Duplicate letters use a two-pass algorithm (greens first, then yellows only while unused letters remain).

## Team
- PAAVNI SARMANDAL – game logic, evaluator, game modes, exceptions
- NIREEKSHA – GUI, word repository, statistics, file handling

## Screenshots
<img width="647" height="988" alt="image" src="https://github.com/user-attachments/assets/f78c5d5d-95ed-4eb4-902c-3f3e2de977b0" />
<img width="600" height="977" alt="image" src="https://github.com/user-attachments/assets/37204652-d14a-4cb5-8ef4-f7846de2a430" />
<img width="616" height="982" alt="image" src="https://github.com/user-attachments/assets/1dc77913-da53-4e2c-ad3b-9b6a422887f9" />


## Possible improvements
Colour the on-screen keys using getKeyboardStates().
Add a statistics window with win percentage and a guess-distribution chart.
Add a daily word, hints, sound effects and dark mode.
Add tile animations and a share-result feature.
Allow different word lengths and languages through configurable word lists and settings.

