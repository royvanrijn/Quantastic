Given a system where all are pointing upwards.
(all OFF)

Z: turn around Z axis (50%), top stays fixed
Y: turn towards you (50%), top goes all the way down
X: turn counterclockwise, top goes to the right all the way down
H: rotate around X and Y


Unknowns:

How do you do a control and use it afterwards?

Shor algorithm:

https://algassert.com/quirk#circuit={%22cols%22:[[1,1,1,1,1,1,1,1,1,1,%22~input%22,1,1,1,%22~guess%22],[1,1,1,1,1,1,1,1,1,1,{%22id%22:%22setR%22,%22arg%22:55},1,1,1,{%22id%22:%22setB%22,%22arg%22:23}],[],[1,1,1,1,1,1,1,1,1,1,%22X%22],[%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22],[1,1,1,1,1,1,1,1,1,1,%22Bloch%22,%22Bloch%22,%22Bloch%22,%22Bloch%22,%22Bloch%22,%22Bloch%22],[%22inputA10%22,1,1,1,1,1,1,1,1,1,%22*BToAmodR6%22],[%22Bloch%22,%22Bloch%22,%22Bloch%22,%22Bloch%22,%22Bloch%22,%22Bloch%22,%22Bloch%22,%22Bloch%22,%22Bloch%22,%22Bloch%22],[%22QFT%E2%80%A010%22],[%22Chance10%22],[1,1,1,1,%22~out%22]],%22gates%22:[{%22id%22:%22~guess%22,%22name%22:%22guess:%22,%22matrix%22:%22{{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}}%22},{%22id%22:%22~input%22,%22name%22:%22input:%22,%22matrix%22:%22{{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}}%22},{%22id%22:%22~out%22,%22name%22:%22out:%22,%22matrix%22:%22{{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}}%22}]}

Just the QFT part of the calculation after preparing A:
https://algassert.com/quirk#circuit={%22cols%22:[[%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22],[%22Z%22,%22%E2%80%A2%22],[%22Bloch%22,%22Bloch%22],[%22Chance10%22],[%22QFT%E2%80%A010%22],[%22Chance10%22]]}

Actual 'simple' gates to do this:
https://algassert.com/quirk#circuit={%22cols%22:[[%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22,%22H%22],[%22Z%22,%22%E2%80%A2%22,%22%E2%80%A2%22],[%22Chance8%22],[%22%E2%80%A6%22,%22%E2%80%A6%22,%22%E2%80%A6%22,%22%E2%80%A6%22,%22%E2%80%A6%22,%22%E2%80%A6%22,%22%E2%80%A6%22,%22%E2%80%A6%22],[%22Swap%22,1,1,1,1,1,1,%22Swap%22],[1,%22Swap%22,1,1,1,1,%22Swap%22],[1,1,%22Swap%22,1,1,%22Swap%22],[1,1,1,%22Swap%22,%22Swap%22],[%22H%22],[%22Z^%C2%BD%22,%22%E2%80%A2%22],[1,%22H%22],[%22Z^%C2%BC%22,%22Z^%C2%BD%22,%22%E2%80%A2%22],[1,1,%22H%22],[%22Z^%E2%85%9B%22,%22Z^%C2%BC%22,%22Z^%C2%BD%22,%22%E2%80%A2%22],[1,1,1,%22H%22],[%22Z^%E2%85%9F%E2%82%81%E2%82%86%22,%22Z^%E2%85%9B%22,%22Z^%C2%BC%22,%22Z^%C2%BD%22,%22%E2%80%A2%22],[1,1,1,1,%22H%22],[%22Z^%E2%85%9F%E2%82%83%E2%82%82%22,%22Z^%E2%85%9F%E2%82%81%E2%82%86%22,%22Z^%E2%85%9B%22,%22Z^%C2%BC%22,%22Z^%C2%BD%22,%22%E2%80%A2%22],[1,1,1,1,1,%22H%22],[%22Z^%E2%85%9F%E2%82%86%E2%82%84%22,%22Z^%E2%85%9F%E2%82%83%E2%82%82%22,%22Z^%E2%85%9F%E2%82%81%E2%82%86%22,%22Z^%E2%85%9B%22,%22Z^%C2%BC%22,%22Z^%C2%BD%22,%22%E2%80%A2%22],[1,1,1,1,1,1,%22H%22],[%22Z^%E2%85%9F%E2%82%81%E2%82%82%E2%82%88%22,%22Z^%E2%85%9F%E2%82%86%E2%82%84%22,%22Z^%E2%85%9F%E2%82%83%E2%82%82%22,%22Z^%E2%85%9F%E2%82%81%E2%82%86%22,%22Z^%E2%85%9B%22,%22Z^%C2%BC%22,%22Z^%C2%BD%22,%22%E2%80%A2%22],[1,1,1,1,1,1,1,%22H%22],[%22Chance8%22]]}

How do you implement this [xB^A mod R]? 
- The A part is clear, [H] on bunch, A is set to output [xB^R mod]
- How to do control NOT and USE it afterwards?
- How B and R?

