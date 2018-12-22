package ru.hitrerros.purse;

import ru.hitrerros.purse.process.ServerRequestProcessor;

class DBMain {

    public static void main(String ...args) throws Exception {
          new ServerRequestProcessor().start();
    }
}
