AngryBird
A libGDX-based Angry Birds game project, created for the CSE 201 Advanced Programming course. This project includes various gameplay elements such as levels, birds, pigs, and a catapult, modeled using object-oriented principles and following best coding practices. This project is structured to run on desktop using LWJGL3.

Project Overview
This Angry Birds project was generated with gdx-liftoff and features the following components:

Platforms
Core: Contains the main game logic shared across all platforms.
lwjgl3: Desktop platform configuration using LWJGL3 for desktop builds and execution.
Features
Levels: Three playable levels with unique structures, pigs, and bird types.
Screens: Includes a homescreen, level selection screen, game screen, and menu screen.
Controls: Mouse/touch interactions for level selection, gameplay actions, and navigation.

Angry Birds Game - Setup and Run Guide
Requirements
Java Development Kit (JDK) - Download and install JDK 8 or higher.
Gradle - The Gradle wrapper is included, so no separate Gradle installation is required.
Easy Steps to Set Up and Run the Game
1. Clone or Download the Project
Clone the repository from your GitHub or local source:
bash
Copy code
git clone https://github.com/your-username/angrybirds-project.git
Alternatively, download the ZIP and extract it to your preferred directory.
2. Open the Project in Your IDE
Use IntelliJ IDEA, Eclipse, or any IDE with Gradle support.
Open the project by selecting the root folder where the project was cloned or extracted.
3. Build the Project
Run the following command to download dependencies and compile the code:

bash
Copy code
./gradlew build
4. Run the Game
To start the game on a desktop environment, use the following command:

bash
Copy code
./gradlew lwjgl3:run
This command launches the game in a window where you can interact with it.

Additional Commands
Clean the Project - Removes build files:

bash
Copy code
./gradlew clean
Run Tests - Executes any unit tests included:

bash
Copy code
./gradlew test
5. Run in an IDE
If you prefer running the game directly in an IDE:

IntelliJ IDEA or Eclipse:
Open the lwjgl3 folder in your IDE.
Find the Lwjgl3Launcher class.
Right-click on Lwjgl3Launcher and select Run.
