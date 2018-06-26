# Quantastic

Quantastic is a very simple Quantum Computer Simulator written in Java.

The main goal for this project at the moment is to prove to myself that I understand how quantum computers work. This code will be part of a presentation I'm writing for some future conferences.

The code is surprisingly short, almost everything is just a matrix calculation done using Apache's `commons-math3`.

## Simulator

The main class in the code (at the moment) is `QSystem`. This class contains the virtual qubits.

The `Gates` class has some predefined gates, including Pauli-gates, Hadamard, C-NOT and even Fourier. Other gates should be easy to implement, they are just matrix calculations after all.

An example of how to use the system can be found in `QuantumComputerSimulator`.

## Example

I've chosen a fluent-API style way of constructing your quantum algorithm, this might change in the future.

Here we have a simple 1-qubit system with one gate, the Hadamard gate.
This gate puts the qubit in a so called `superposition`. Much like Schrödingers Cat we don't know what the value is, only once measued we know if the cat survived.

```java
        new QSystem(1)
                .applyGate(Gates.HADAMARD_GATE, 0)
                .print()
                .measure();
```

Output:

```
Applying gate:
	(0.7071067811865475, 0.0)(0.7071067811865475, 0.0)
	(0.7071067811865475, 0.0)(-0.7071067811865475, 0.0)
Current state of system:
	0: (0.7071067811865475, 0.0)
	1: (0.7071067811865475, 0.0)
Chances for qubits measured ON:
	0: 0.50%

Measurement:
	Value: 0.5934134608134609
	State: 1
```

State: 1... our cat is dead.

## Future

I might add more functionality in the future, hoping to be able to implement more advanced Quantum Algorithms like Shor.

Also: Keep an eye out for the presentation I'm making for a conference near you! (Maybe J-Fall/Devoxx?)

_(Do you want me to speak about quantum computing at your conference? Ask me!)_
