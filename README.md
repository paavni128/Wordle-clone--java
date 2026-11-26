# Wordle Clone (Java)

A desktop Wordle game built in Java with Swing, made for our OOP course project.

## Features
- 6 attempts to guess a 5-letter word, with green / yellow / grey tile feedback
- Correct handling of duplicate letters
- Dictionary validation (non-words are rejected)
- On-screen keyboard that colours itself as you play
- Normal and Hard mode (Hard forces you to reuse revealed hints)
- Restart button for a new random word
- Player statistics saved to a file (games played, win %, streak)

## How to run
Requires JDK 17.0.15

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
[add 2-3 screenshots here: normal game, win popup, hard mode error]

## Possible improvements
Dark mode, hint button, daily word, sound effects.
