# WhatsASI

At INSA Rouen, in Java 8 and using the RMI technology as part of a **Distributed Computing** course, creation of an instant messaging application, enabling users to chat from different machines. 

Users can choose a pseudo, a profile avatar, and decide in which conversation they want to chat. 

It is also possible to chat with an *artificial intelligence* (AI). 

Finally, the user can also select words that he wants the application to filter.

There are two graphic interfaces available : 

    - one developed thanks to JavaFX
    - the other one to be used in a terminal.

## Documentation

See :

- the WhatsASI code in the *WhatsASI* folder
- the specifications in the *DS* folder 
- the architecture design of the application in the *DC* folder
- the full report of the project in the *RAPPORT FINAL* folder

## Compilation instructions 

1.  Before compiling, set the ENDPOINT corresponding to your server's url in ClientTerminal or MessagerieClient.

2.  `./compile.sh` compile client et server

## Execution instructions 

### Server side 

1. `./launchRmiregistry.sh`
2. `./launchServer.sh`

### Client side

1.  `./launchClient [terminal]` set argument to terminal if you want to use the terminal mode, leave it empty else.
