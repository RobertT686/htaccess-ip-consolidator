## .htaccess IP address formatter

The purpose of this program is to take multiple lists of IP addresses and merge them together into a format used by Apache's .htaccess files, with similar IP addresses sorted near each other and IP addresses with identical subnets on the same line (ex. x.x.0.1 and x.x.0.2 vs. x.y.0.3).

This program came about as I was trying to build a .httaccess file for my personal website to deny certain IP addresses from visiting; I found multiple large lists of IP addresses from different sources, all of which had a single IP address on each line. I wanted to consolidate all of them into a single sorted list to make any future updates easier.

The easiest way to run the program is to compile it into a .jar and then run it from the command line using "java -jar *filename.jar* *lists of IP addresses*"

Things that I need to change:
- [x] Switch to "Require ip" syntax.
  - This will allow me to also make the output more generic, rather than specifically denying the input addresses.
- [x] Add IPv6 compatibility.
- [ ] Improve sorting so that it doesn't put an address like 10.2.x.x next to the line for 10.200.x.x and instead puts it between the lines 10.1.x.x and 10.3.x.x like one would expect.
