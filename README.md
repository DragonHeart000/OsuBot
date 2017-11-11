# OsuBot

Osu!Bot 3.0 is a client sided irc chat bot made to help streamers manage song requests going on in twitch chat from osu chat! Osu!Bot will listen to twitch chat for any song requests, or other commands that are supported, from non banned users and add them to a queue. Upon seeing an admin request the next song in twitch chat or the user ask for the next song in game chat Osu!Bot will provide the next song that was queued in both Twitch and Game chat.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

Java 9 or later is required to run Osu!Bot.

## Deployment

Given that you have Java 9 or newer installed you are able to just run the jar file to execute Osu!Bot and start.

If you downloaded the source you are able to compile the project into a jar file using your prefered option. Be sure to have Maven for the dependencies.

All saves are currently saved in C://osuBot/ However, this may be changed in the future and likely will be at least for certain Operating Systems.

### Skinning

Skinning is not yet supported, however, the current plan is to let users use any CSS file to skin Osu!Bot but there will be multiple offical skins packaged with it and more to download on my website.

## Built With

* [PircBotX](https://github.com/TheLQ/pircbotx) - IRC Library
* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

Please read [CONTRIBUTING.md]() for details on the process for submitting pull requests to me.

## Versioning

MAJOR.MINOR.PATCH

MAJOR version when you make incompatible API changes,
MINOR version when you add functionality in a backwards-compatible manner
PATCH version when you make backwards-compatible bug fixes

## Authors

* **Riley Shumway** - *Initial work, updates, and managment* - [PurpleBooth](https://github.com/DragonHeart000)

See also the list of [contributors](https://github.com/DragonHeart000/OsuBot/graphs/contributors) who participated in this project.

## License

This project is licensed under the GNU General Public License - see the [LICENSE.md](https://github.com/DragonHeart000/OsuBot/blob/master/LICENSE) file for details

## Acknowledgments

I'd like to thank everyone who visits my stream and put Osu!Bot 1 and 2 to good use. After seeing how well it worked I thought it was a good idea to make a public build for everyone to use! (Special thanks to [Valaraan_](https://www.twitch.tv/valaraan_), [PasiPasass](https://go.twitch.tv/pasipasass), and [edgelordmastermemesupreme](https://go.twitch.tv/edgelordmastermemesupreme))

Thanks to PircBotX for making a easy to use IRC Library!

Thanks to mikuia for not ever working for me when I wanted it forcing me to make my own more reliable bot ;)
