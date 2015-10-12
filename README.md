# magic-wand
## What is this?
magic-wand is a helper for creating WebDrivers.  This allows test to easily rely on different WebDrivers depending on different system requirements.  Currently there are targets to allow for generation of:

 - locally connected:
 - mobile phones
 - web browsers
 - dawg house connected:
 - mobile phones
 - saucelabs web browsers

## How do I use it?
When magic-wand runs, it loads drivers from the class path and the local environment.  The factories that generate the drivers all inherit from `WizardFactory`.  The search path can be expanded from the packages on the class path so that it examines URLs in the MAGICWAND_PACKAGES environment and system properties paths.  Multiple URLs can be entered in these entries.  Entries are delimited by semicolons.
