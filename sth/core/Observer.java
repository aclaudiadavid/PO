package sth.core;

interface Observer{
	void sendNotification(String s);

	void addToList(Person p);

	void removeFromList(Person p);
}