package com.billygoatpharmacy.ecobeestinger.ecobeeObjects;

public class Settings 
{
	public String hvacMode;/** The current HVAC mode the thermostat is in. Values: auto, auxHeatOnly, cool, heat, off. **/
	public String lastServiceDate;/** The last service date of the HVAC equipment. **/
	public Boolean serviceRemindMe;/** Whether to send an alert when service is required again. **/
	public int monthsBetweenService;/** The user configured monthly interval between HVAC service reminders **/
	public String remindMeDate;/** Date to be reminded about the next HVAC service date. **/
	public String vent;/** The ventilator mode. Values: auto, minontime, on, off. **/
	public int ventilatorMinOnTime;/** The minimum time in minutes the ventilator is configured to run. The thermostat will always guarantee that the ventilator runs for this minimum duration whenever engaged. **/
	public Boolean serviceRemindTechnician;/** Whether the technician associated with this thermostat should receive the HVAC service reminders as well. **/
	public String eiLocation;/** A note about the physical location where the SMART or EMS Equipment Interface module is located. **/
	public int coldTempAlert;/** The temperature at which a cold temp alert is triggered. **/
	public Boolean coldTempAlertEnabled;/** Whether cold temperature alerts are enabled. **/
	public int hotTempAlert;/** The temperature at which a hot temp alert is triggered. **/
	public Boolean hotTempAlertEnabled;/** Whether hot temperature alerts are enabled. **/
	public int coolStages;/** The number of cool stages the connected HVAC equipment supports. **/
	public int heatStages;/** The number of heat stages the connected HVAC equipment supports. **/
	public int maxSetBack;/** The maximum automated set point set back offset allowed in degrees. **/
	public int maxSetForward;/** The maximum automated set point set forward offset allowed in degrees. **/
	public int quickSaveSetBack;/** The set point set back offset, in degrees, configured for a quick save event. **/
	public int quickSaveSetForward;/** The set point set forward offset, in degrees, configured for a quick save event. **/
	public Boolean hasHeatPump;/** Whether the thermostat is controlling a heat pump. **/
	public Boolean hasForcedAir;/** Whether the thermostat is controlling a forced air furnace. **/
	public Boolean hasBoiler;/** Whether the thermostat is controlling a boiler. **/
	public Boolean hasHumidifier;/** Whether the thermostat is controlling a humidifier. **/
	public Boolean hasErv;/** Whether the thermostat is controlling an energy recovery ventilator. **/
	public Boolean hasHrv;/** Whether the thermostat is controlling a heat recovery ventilator. **/
	public Boolean condensationAvoid;/** Whether the thermostat is in frost control mode. **/
	public Boolean useCelsius;/** Whether the thermostat is configured to report in degrees Celcius. **/
	public Boolean useTimeFormat12;/** Whether the thermostat is using 12hr time format. **/
	public String locale;/** Multilanguage support, currently only "en" - english is supported. In future others locales can be supported. **/
	public String humidity;/** The minimum humidity level (in percent) set point for the humidifier **/
	public String humidifierMode;/** The humidifier mode. Values: auto, manual, off. **/
	public int backlightOnIntensity;/** The thermostat backlight intensity when on. A value between 0 and 10, with 0 meaning 'off' - the zero value may not be honored by all ecobee versions. **/
	public int backlightSleepIntensity;/** The thermostat backlight intensity when asleep. A value between 0 and 10, with 0 meaning 'off' - the zero value may not be honored by all ecobee versions. **/
	public int backlightOffTime;/** The time in seconds before the thermostat screen goes into sleep mode. **/
	public int soundTickVolume;/** The volume level for key presses on the thermostat. A value between 0 and 10, with 0 meaning 'off' - the zero value may not be honored by all ecobee versions. **/
	public int soundAlertVolume;/** The volume level for alerts on the thermostat. A value between 0 and 10, with 0 meaning 'off' - the zero value may not be honored by all ecobee versions. **/
	public int compressorProtectionMinTime;/** The minimum time the compressor must be off for in order to prevent short-cycling. **/
	public int compressorProtectionMinTemp;/** The minimum outdoor temperature that the compressor can operate at - applies more to air source heat pumps than geothermal. **/
	public int stage1HeatingDifferentialTemp;/** The difference between current temperature and set-point that will trigger stage 2 heating. **/
	public int stage1CoolingDifferentialTemp;/** The difference between current temperature and set-point that will trigger stage 2 cooling. **/
	public int stage1HeatingDissipationTime;/** The time after a heating cycle that the fan will run for to extract any heating left in the system - 30 second default. **/
	public int stage1CoolingDissipationTime;/** The time after a cooling cycle that the fan will run for to extract any cooling left in the system - 30 second default (for not) **/
	public Boolean heatPumpReversalOnCool;/** The flag to tell if the heat pump is in heating mode or in cooling when the relay is engaged. If set to zero it's heating when the reversing valve is open, cooling when closed and if it's one - it's the opposite. **/
	public Boolean fanControlRequired;/** Whether fan control by the Thermostat is required in auxiliary heating (gas/electric/boiler), otherwise controlled by furnace. **/
	public int fanMinOnTime;/** The minimum time, in minutes, to run the fan each hour. Value from 1 to 60. **/
	public int heatCoolMinDelta;/** The minimum temperature difference between the heat and cool values. Used to ensure that when thermostat is in auto mode, the heat and cool values are separated by at least this value. **/
	public int tempCorrection;/** The amount to adjust the temperature reading in degrees F - this value is subtracted from the temperature read from the sensor. **/
	public String holdAction;/** The default end time setting the thermostat applies to user temperature holds. ValuesuseEndTime4hour, useEndTime2hour (EMS Only), nextPeriod, indefinite, askMe **/
	public Boolean heatPumpGroundWater;/** Whether the Thermostat uses a geothermal / ground source heat pump. **/
	public Boolean hasElectric;/** Whether the thermostat is connected to an electric HVAC system. **/
	public Boolean hasDehumidifier;/** Whether the thermostat is connected to a dehumidifier. If true or dehumidifyOvercoolOffset > 0 then allow setting dehumidifierMode and dehumidifierLevel. **/
	public String dehumidifierMode;/** The dehumidifier mode. Values: on, off. If set to off then the dehumidifier will not run, nor will the AC overcool run. **/
	public int dehumidifierLevel;/** The dehumidification set point in percentage. **/
	public Boolean dehumidifyWithAC;/** Whether the thermostat should use AC overcool to dehumidify. When set to true a postive int value must be supplied fordehumidifyOvercoolOffsetotherwise an API validation exception will be thrown. **/
	public int dehumidifyOvercoolOffset;/** Whether the thermostat should use AC overcool to dehumidify and what that temperature offset should be. A value of 0 means this feature is disabled and dehumidifyWithACwill be set to false. Value represents the value in F to subract from the current set point. Values should be in the range 0 - 50 and be divisible by 5. **/
	public Boolean autoHeatCoolFeatureEnabled;/** If enabled, allows the Thermostat to be put in HVACAuto mode. **/
	public Boolean wifiOfflineAlert;/** Whether the alert for when wifi is offline is enabled. **/
	public int heatMinTemp;/** The minimum heat set point allowed by the thermostat firmware. **/
	public int heatMaxTemp;/** The maximum heat set point allowed by the thermostat firmware. **/
	public int coolMinTemp;/** The minimum cool set point allowed by the thermostat firmware. **/
	public int coolMaxTemp;/** The maximum cool set point allowed by the thermostat firmware. **/
	public int heatRangeHigh;/** The maximum heat set point configured by the user's preferences. **/
	public int heatRangeLow;/** The minimum heat set point configured by the user's preferences. **/
	public int coolRangeHigh;/** The maximum cool set point configured by the user's preferences. **/
	public int coolRangeLow;/** The minimum heat set point configured by the user's preferences. **/
	public String userAccessCode;/** The user access code value for this thermostat. See theSecuritySettings object for more information. **/
	public int userAccessSetting;/** The int representation of the user access settings. See theSecuritySettings object for more information. **/
	public int auxRuntimeAlert;/** The temperature at which an auxHeat temperature alert is triggered. **/
	public int auxOutdoorTempAlert;/** The temperature at which an auxOutdoor temperature alert is triggered. **/
	public int auxMaxOutdoorTemp;/** The maximum outdoor temperature above which aux heat will not run. **/
	public Boolean auxRuntimeAlertNotify;/** Whether the auxHeat temperature alerts are enabled. **/
	public Boolean auxOutdoorTempAlertNotify;/** Whether the auxOutdoor temperature alerts are enabled. **/
	public Boolean auxRuntimeAlertNotifyTechnician;/** Whether the auxHeat temperature alerts for the technician are enabled. **/
	public Boolean auxOutdoorTempAlertNotifyTechnician;/** Whether the auxOutdoor temperature alerts for the technician are enabled. **/
	public Boolean disablePreHeating;/** Whether the thermostat should use pre heating to reach the set point on time. **/
	public Boolean disablePreCooling;/** Whether the thermostat should use pre cooling to reach the set point on time. **/
	public Boolean installerCodeRequired;/** Whether an installer code is required. **/
	public String drAccept;/** Whether Demand Response requests are accepted by this thermostat. Possible values are:always, askMe, customerSelect, defaultAccept, defaultDecline, never. **/
	public Boolean isRentalProperty;/** Whether the property is a rental, or not. **/
	public Boolean useZoneController;/** Whether to use a zone controller or not. **/
	public int randomStartDelayCool;/** Whether random start delay is enabled for cooling. **/
	public int randomStartDelayHeat;/** Whether random start delay is enabled for heating. **/
	public int humidityHighAlert;/** The humidity level to trigger a high humidity alert. **/
	public int humidityLowAlert;/** The humidity level to trigger a low humidity alert. **/
	public Boolean disableHeatPumpAlerts;/** Whether heat pump alerts are disabled. **/
	public Boolean disableAlertsOnIdt;/** Whether alerts are disabled from showing on the thermostat. **/
	public Boolean humidityAlertNotify;/** Whether humidification alerts are enabled to the thermsotat owner. **/
	public Boolean humidityAlertNotifyTechnician;/** Whether humidification alerts are enabled to the technician associated with the thermsotat. **/
	public Boolean tempAlertNotify;/** Whether temperature alerts are enabled to the thermsotat owner. **/
	public Boolean tempAlertNotifyTechnician;/** Whether temperature alerts are enabled to the technician associated with the thermostat. **/
	public int monthlyElectricityBillLimit;/** The dollar amount the owner specifies for their desired maximum electricy bill. **/
	public Boolean enableElectricityBillAlert;/** Whether electricity bill alerts are enabled. **/
	public Boolean enableProjectedElectricityBillAlert;/** Whether electricity bill projection alerts are enabled **/
	public int electricityBillingDayOfMonth;/** The day of the month the owner's electricty usage is billed. **/
	public int electricityBillCycleMonths;/** The owners billing cycle duration in months. **/
	public int electricityBillStartMonth;/** The annual start month of the owners billing cycle. **/
	public int ventilatorMinOnTimeHome;/** The number of minutes to run ventilator per hour when home. **/
	public int ventilatorMinOnTimeAway;/** The number of minutes to run ventilator per hour when away. **/
	public Boolean backlightOffDuringSleep;/** Determines whether or not to turn the backlight off during sleep. **/
	public Boolean autoAway;/** When set to true if no occupancy motion detected thermostat will go into indefinite away hold, until either the user presses resume schedule or motion is detected. **/
	public Boolean smartCirculation;/** When set to true if a larger than normal delta is found between sensors the fan will be engaged for 15min/hour. **/
	public Boolean followMeComfort;/** When set to true if a sensor has detected presense for more than 10 minutes then include that sensor in temp average. If no activity has been seen on a sensor for more than 1 hour then remove this sensor from temperature average. **/
	public String ventilatorType;/** This read-only field represents the type of ventilator present for the Thermostat. The possible values are none, ventilator, hrv, and erv. **/
	public Boolean isVentilatorTimerOn;/** This Boolean field represents whether the ventilator timer is on or off. The default value is false. If set to true the ventilatorOffDateTime is set to now() + 20 minutes. If set to falsethe ventilatorOffDateTime is set to it's default value. **/
	public String ventilatorOffDateTime;/** This read-only field represents the Date and Time the ventilator will run until. The default value is 2014-01-01 00:00:00. **/
	public Boolean hasUVFilter;/** This Boolean field represents whether the HVAC system has a UV filter. The default value is true. **/
	public Boolean coolingLockout;/** This field represents whether to permit the cooling to operate when the Outdoor temeperature is under a specific threshold, currently 55F. The default value is false. **/
	public Boolean ventilatorFreeCooling;/** Whether to use the ventilator to dehumidify when climate or calendar event indicates the owner is home. The default value is false. **/
	public Boolean dehumidifyWhenHeating;/** This field represents whether to permit dehumidifer to operate when the heating is running. The default value is false. **/
	public Boolean ventilatorDehumidify;/** This field represents whether or not to allow dehumification when cooling. The default value is true. **/
	public String groupRef;/** The unique reference to the group this thermostat belongs to, if any. See GET Group request and POST Group request for more information. **/
	public String groupName;/** The name of the the group this thermostat belongs to, if any. SeeGET Group request and POST Group request for more information. **/
	public int groupSetting;/** The setting value for the group this thermostat belongs to, if any. SeeGET Group request and POST Group request for more information. **/
}
