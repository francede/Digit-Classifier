# Handwritten Digit Classifier

This project is an implementation of an multilayer neural network that can learn to recognize handwritten digits as well as digits drawn to the GUI. It is currently configured and optimized to work with the [MNIST database](http://yann.lecun.com/exdb/mnist/ "MNIST database."), but the implementation allows for any number of inputs, layer sizes and outputs.

#### Network setup and optimization
------------

The network is a multilayer neural network. It uses Sigmoid function as the activation function.

The input layer consists of 784 inputs, one for each pixel in the grayscale trainingset images of 28x28 pixels. The output layer size is 10, each output representing a category for numbers 0-9.

After testing the network with MNIST training set with multiple different layer configurations, the best results were achieved with two hidden layers of sizes 288 and 48 respectively. The project has a crude class for testing and comparing different setups.

#### Dataset
------------

The MNIST training set consists of 60000 and handwritten grayscale 28x28 images and the testing set consists of 10000 images.

#### Other specs
------------

Hibernate is used to map the neural network to SQL-based database and vice versa allowing quick recalling and saving of previous weights and biases.

The GUI is created with JavaFX. The project implements the MVC architectural pattern. The project is a Maven project. The required libraries are JavaFX, MySQLConnector and Hibernate.

Yann LeCun and Corinna Cortes hold the copyright of MNIST database. The IDX Image File Reader is based on a solution presented in [ Stackoverflow by RayDeeA](https://stackoverflow.com/questions/17279049/reading-a-idx-file-type-in-java "RayDeeA").


No external APIs or frameworks are used for the neural network. The project was done as a school project in Metropolia UAS.
