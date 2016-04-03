package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class NotificationSettings 
{
	public String[] emailAddresses;/** The list of email addresses alerts and reminders will be sent to. The full list of email addresses must be sent in any update request. If any are missing from that list they will be deleted. If an empty list is sent, any email addresses will be deleted. **/
	public Boolean emailNotificationsEnabled;/** Boolean values representing whether or not alerts and reminders will be sent to the email addresses listed above when triggered. **/
	public EquipmentSetting[] equipment;/** The list of equipment specific alert and reminder settings. **/
	public GeneralSetting[] general;/** The list of general alert and reminder settings. **/
	public LimitSetting[] limit;/** The list of limit specific alert and reminder settings. **/
}
