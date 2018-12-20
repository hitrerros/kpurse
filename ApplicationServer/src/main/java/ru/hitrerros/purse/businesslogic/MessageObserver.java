package ru.hitrerros.purse.businesslogic;

import ru.hitrerros.purse.message.Message;

public interface MessageObserver {

    public void onGetMessage(Message message);

    public void dummySender();



}
