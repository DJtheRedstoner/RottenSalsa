# RottenSalsa

A partial implementation of [RFC 7539](https://datatracker.ietf.org/doc/html/rfc7539) I created during [an internet outage](https://blog.cloudflare.com/cloudflares-view-of-the-rogers-communications-outage-in-canada/).
While this implementation (hopefully) passes  the test vectors provided in the RFC, it **IS NOT** audited and **SHOULD NOT** be used.

## How I did it

As a data hoarder, I have all the IETF's RFCs downloaded (you can download them for yourself [here](https://www.rfc-editor.org/retrieve/bulk/)).
Turns out, this is rather useful to combat boredom during an internet outage.

## The name
ChaCha20 is closely related to [Salsa20](https://en.wikipedia.org/wiki/Salsa20). This implementation should never be trusted
or used, hence "Rotten".