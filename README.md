# AuthX
Hytale plugin for authorization/registration on the server

It has a config for configuring parameters such as:
- Data for communication with the database.
- Setting the minimum number of characters for the password.
- Language settings(Attention! The translation was done using AI (except ru) (I HOPE SOMEONE READ THIS)).
- Setting up the name of the table that will be created in the database.
- Setting the server name (window title).

# Requirements:
- MySQL
- Hytale Server

The plugin is currently under testing, so feel free to write bug reports, it will be interesting for me to fix bugs.

It is also possible to add your own translation (although you will have to rebuild the plugin, the source code contains files with the extension in the resources.properties, DO NOT TOUCH THE NAME OF THE KEYS TO AVOID MISTAKES!).

Existing languages at the moment: ru, en, de, it, fr, es, pt.(for configuration)

The plugin was written to familiarize you with the Hytale API, so there may be errors at the amateur level (By the way, I really liked the API).

Special thanks to the Discord server hytale-docs, for the help provided in the study.

The plugin will still be finalized.

# Commands:
/auth reload(required:"authx.*") - reload database
/changepassword(required:"authx.changepassword") - change your password
