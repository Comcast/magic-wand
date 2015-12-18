Magic-Wand
==========
[![Build Status](https://travis-ci.org/Comcast/magic-wand.svg)](https://travis-ci.org/Comcast/magic-wand)

[http://comcast.github.io/magic-wand](http://comcast.github.io/magic-wand)

##Summary

Magic-Wand is a helper for creating WebDrivers.  This allows test to easily rely on different WebDrivers depending on different system requirements.  Currently there are targets to allow for generation of:

 - locally connected:
   - mobile phones
   - web browsers
 - dawg house connected:
   - mobile phones
   - saucelabs web browsers

![Magic-wand](http://comcast.github.io/magic-wand/images/magic-wand-shield.png)

## How do I use it?
When magic-wand runs, it loads drivers from the class path and the local environment.  The factories that generate the drivers all inherit from `WizardFactory`.  The search path can be expanded from the packages on the class path so that it examines URLs in the MAGICWAND_PACKAGES environment and system properties paths.  Multiple URLs can be entered in these entries.  Entries are delimited by semicolons.

##Submitting Issues
Please file a github issue for any problems or feature requests (or better yet, submit a pull request!)
