# 🤖 ChatBot by Raj Kothari

An Android chatbot application built using **Java** and **XML layouts** in **Android Studio** with **Gemini Integration**.  
The app features a conversational UI with a bot and user message interface, custom message bubbles, avatars, dark mode compatibility, saving and deleting chats functionality

---

## ✨ Features

- 🟢 **Saving & Deleting Chats**  
  - Saves Chat even after closing or reopening app
  - Clears chat with 1 click

- 💬 **Chat Interface**  
  - User messages (right-aligned with avatar).  
  - Bot responses (left-aligned with avatar).  
  - Custom **chat bubbles** with rounded corners.  

- 🎨 **Dark & Light Mode Support**  
  - Auto adapts based on system theme.  

- 📜 **Dynamic Message Bubbles**  
  - Messages auto-expand vertically.  
  - Scrolling enabled for long conversations.  

- 🌐 **Internet & Voice Support (Optional)**  
  - Internet permission included for API integration.  
  - Microphone permission added for future **voice input**.  

---

## 📂 Project Structure

## Java/Kotlin Source Files
`app/src/main/java/com/example/assignment1c030RajKothari/`
- **MainActivity.java** – Main chat interface where user interacts with the bot.  
- **MessageAdapter.java** – RecyclerView adapter for displaying chat messages.  
- **TextFormatter.java** *(Optional)* – Handles text formatting logic.  
- **Opening.java** – Splash screen activity displayed at app launch.  

## Layout Resources
`app/src/main/res/layout/`
- **activity_main.xml** – Layout for the main chat screen.  
- **item_message_user.xml** – Layout template for messages sent by the user.  
- **item_message_bot.xml** – Layout template for messages sent by the bot.  
- **activity_opening.xml** – Layout for the splash screen.  

## Drawable Resources
`app/src/main/res/drawable/`
- **bg_bubble_user.xml** – Background style for user chat bubbles.  
- **bg_bubble_bot.xml** – Background style for bot chat bubbles.  
- **circle_background.xml** – Circular background for avatars.  
- **chatbot.jpg** – Logo displayed on the splash screen.  
- **user.jpg / bot.jpg** – Avatar images for user and bot messages. 

## Getting Started

Follow the steps below to set up your development environment and run the project.

---

## 1. Get a Gemini API Key

1. Go to the [Google AI Studio](https://aistudio.google.com/).
2. Sign in with your Google account.
3. Navigate to **Get API Key** under your account or project settings.
4. Copy your newly generated **API Key**.
5. Save it securely — you’ll need it in the project.

---

## 2. Set Up Android Studio

1. [Download and install Android Studio](https://developer.android.com/studio).
2. Launch Android Studio and complete the initial setup wizard.
   - Install the latest **Android SDK** and **Android SDK Platform Tools**.
   - Ensure **Android Virtual Device (AVD)** is installed for emulator testing.
3. Open Android Studio and select **File > Settings > Plugins**.
   - (Optional) Install additional plugins if required by the project.

---

## 3. Clone the Repository

1. Open a terminal and run:

   ```bash
   git clone https://github.com/your-username/your-repo.git
