# visual-ptolemy
For visualizing paths of planets on epicycles per the Ptolemaic model of the universe.

![gif showing the result of the program](https://github.com/lourosenb/visual-ptolemy/blob/main/example_graphic.gif)

## Features
- Move "time" forward and backward in large steps using `A` and `D` or smaller steps using `Q` and `E`
- Change which planet is depicted by altering `EPICYCLE_RETURNS` and `PER_THIS_MANY_ROTATIONS` in the code
- Press `SPACE` to keep the current position visible on screen
- Press `DELETE` (and then advance time) to get rid of the path traced thus far
- See the angle between the previous time step and the current one (traced in pink)
- See the epicycle (traced in green)
- See the planet's current position (black dot) and the path traced by it (traced in cyan)

## Suggested Values, from Ptolemy's _Almagest_
### Venus (Default)
- `EPICYCLE_RETURNS`: 5
- `PER_THIS_MANY_ROTATIONS`: 8
### Mercury
- `EPICYCLE_RETURNS`: 145
- `PER_THIS_MANY_ROTATIONS`: 46
