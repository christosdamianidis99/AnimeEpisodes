# Anime Episodes Tracker App

## Overview
The Anime Episodes Tracker App is an Android application designed to help users manage their anime watchlist. 
It integrates with an external API to fetch anime data and provides features for storing and tracking watched episodes. 
Users can browse a list of anime, select their favorites, and organize them based on different genres.

## Features
-**API Integration**: Fetches 2000 anime records from the Anime DB API upon first launch.

-**Anime List**: Displays a list of anime with details and photos in a ListView.

-**Episode Tracking**: Allows users to track which episode they are currently watching.

-**Genre-Based Filtering**: Use the navigation pane to filter anime by genre (e.g., Action, Adventure, Drama).


## Getting Started
## Prerequisites
-Android Studio
-Java Development Kit (JDK) 8 or higher
-Access to the [Anime DB API](https://rapidapi.com/brian.rofiq/api/anime-db) (API key may be required)


## Installation
-**Clone the Repository**

git clone https://github.com/yourusername/your-repo-name.git
cd your-repo-name

-**Open the Project**

Open Android Studio and select Open an existing project, then navigate to the directory where you cloned the repository.

-**Setup API Key**

Obtain an API key from RapidAPI and add it to your project. Place the API key in a secure manner, typically in a configuration file or environment variable.

-**Sync and Build**

Sync your project with Gradle files and build the project to ensure all dependencies are properly configured.


## Usage
-**Initial Data Fetch**

Upon running the app for the first time, it will make an API call to fetch 2000 anime records and store them in a local database.

-**Explore Anime**

Navigate to the screen with the ListView to browse through the fetched anime. Each item displays details such as the title, description, and an image.

-**Track Episodes**

From the ListView, users can select an anime to add to their personal watchlist. They can also input which episode they are currently watching.

-**Filter by Genre**

Use the navigation pane to filter the list of anime by genre (e.g., Action, Adventure, Drama). This allows users to view anime based on their preferences.


## Contributing
Contributions are welcome! If you find any bugs or have suggestions for improvements, please feel free to open an issue or submit a pull request.


## Acknowledgements
-**Anime DB API**: Provided by BrianRofiq.

-**RapidAPI**: For API hosting and management.
