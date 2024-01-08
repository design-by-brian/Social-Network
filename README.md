## MAIN RUNNING
The main file to run is SocialSim. This takes a number of command lines from the terminal to determine what mode
to run in (Interactive or Simulation) and which files to read in. This process has been made some what more user friendly
by implementing a set of simple bash scripts. This is mainly due to the fact that an external graph visualisation library
is implemented which means the class path must be appended for running and compiling.

# COMPILATION
To compile all the java files provided in this project please us the bash script "./compile.sh". This will append the
appropriate class paths and allow the external library to be used at compile time.

# RUNNING
To run SocialSim please us the bash script "./run.sh". This once again appends the appropriate class paths for GraphStream.
using the run bash script with no command line arguments will provide usage information to the terminal. Along with a display
of all the available network and event files. providing the argument "-i" along with run bash script
will launch the program in interactive mode. To run using simulation mode, use the run bash script along with the
following command line arguments. "-s <networkFile> <eventFile> <prob_like> <prob_foll>".

# CLEAN
To remove all class files from the project directory, the "./clean.sh" bash script can be used.

# FILE STRUCTURE
The main directory Social_Network contains all project files and three subdirectories. InputFiles contains two subdirectories
which hold the network and event files. Place any input files you want to use for testing in here. The program is looking for
them in these directories. You can check that a file is in the correct directory by using "./run.sh" with no command
line arguments. GraphStreamLib contains all the .jar files for the external graph visualisation library. This directory
also contains a "stylesheet.css". This file provides information on graph displaying to the GraphStream class. Finally
a subdirectory called SimLogs is used for storing the event logs produced by running SocialSim in simulation mode.

## FILE DESCRIPTIONS
# SocialSim.java
Main program used for running the social network project. Run using "./run.sh".

# Interactive.java
Class for running the interactive user interface aspect of social sim. Allows the suer to load in a network file and
test various functions.

# Simulation.java
Class for running the simulation aspect of SocialSim. Mainly uses FilIO reading in the network and event file provided.

# GraphStream.java
Class for visualising network to the user. Displays as a grpah of nodes and edges. More follwers = more red/larger node.

# SocialNetwork.java
This is the main class for storing data related to a social network. This class manages the SocialNetwork object and is
used by almost all other class files in this project.

# Post.java
Class for managing a Post object.

# Person.java
Class for managing a Person object.

# DSAHashTable.java
Class for managing a DSAHashTable object

# DSAGraph.java
Class for managing a DSAGraph object

# DSALinkedList.java
Class for managing a DSALinkedList object

# DSAQueue.java
Class for managing a DSAQueue object

# UnitTestDSAHashTable.java
Class for testing the functionality of a DSAHashtable

# UnitTestDSAGraph.java
Class for testing the functionality of a DSAGraph

# UnitTestDSALinkedList.java
Class for testing the functionality of a DSALinkedList

# UnitTestDSAQueue.java
Class for testing the functionality of a DSAQueue

# RandomEventFile.java
Class that creates a random event file from a provided network file.

# run.sh
Bash script for running SocialSim with dependencies/correct class path.

# compile.sh
Bash script for compiling all java files.

# clean.sh
Bash script for removing all class files.
