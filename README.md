# Stickers for Hangouts

An app and Xposed module that adds sticker functionality to Hangouts on Android

## Dependencies
Crouton: https://github.com/keyboardsurfer/Crouton

Aviary: https://developers.aviary.com/

AppCompat: http://developer.android.com/tools/support-library/features.html

android-gif-drawable: https://github.com/koral--/android-gif-drawable

Picasso: https://github.com/square/picasso

Sugar: https://github.com/satyan/sugar

Modified version of Color Picker Collection to work with Support library: http://d-h.st/KpE (original here: https://github.com/gabrielemariotti/colorpickercollection)

## Here to remove the ads?
If you want to build your own personal version without ads, do the following:

In "activity_sticker_picker.xml", set the height of the banner to 1px;
In "UserFragment.java", remove the segment from "int min = 0;" to the line above "v = ..."

Build your version.
All I ask if you do this is that you do not share it!
