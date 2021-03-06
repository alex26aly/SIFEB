/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import com.sifeb.ve.resources.SifebUtil;
import com.sifeb.ve.resources.Strings;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * This is the Device Class which is used for implementing any device module
 * @author Udith Arosha
 */
public class Device {

    // Device Type Definitions /////////////////////////
    public static final String DEV_ACTUATOR = "actuator";
    public static final String DEV_SENSOR = "sensor";
    ////////////////////////////////////////////////////

    private final String deviceID;
    private final Map<Locale, String> deviceNames;
    private final int address;
    private final String type;
    private final String imgName;
    private final Image image;
    private final ArrayList<Capability> capabilities;
    private final DeviceBlock deviceBlock;

    public Device(String deviceID, Map<Locale, String> deviceNames, int address, String type, String imageName) {
        this.deviceID = deviceID;
        this.deviceNames = deviceNames;
        this.address = address;
        this.type = type;
        this.imgName = imageName;
        this.image = new Image("file:" + SifebUtil.DEVICE_IMG_DIR + imageName + ".png");
        this.deviceBlock = new DeviceBlock(this);
        this.capabilities = new ArrayList<>();
    }

    // returns the device Id
    public String getDeviceID() {
        return deviceID;
    }

    // returns the device address
    public int getAddress() {
        return address;
    }

    // returns the device type
    public String getType() {
        return type;
    }

    // returns the device image
    public Image getImage() {
        return image;
    }

    // returns device's capabilities
    public ArrayList<Capability> getCapabilities() {
        return capabilities;
    }
    
    // add capabilities to the device
    public void addCapabilities(ArrayList<Capability> caps){
        this.capabilities.addAll(caps);
    }

    // returns device name
    public String getDeviceName() {
        Locale currentLocale = Strings.getLocale();
        return deviceNames.get(currentLocale);
    }

    // returns device name based on the locale
    public String getDeviceName(Locale locale) {
        return deviceNames.get(locale);
    }

    // returns device block
    public DeviceBlock getDeviceBlock() {
        return deviceBlock;
    }

    // returns device names mapping
    public Map<Locale, String> getDeviceNames() {
        return deviceNames;
    }

    // returns image name
    public String getImgName() {
        return imgName;
    }
    
    // add device block to the pane
    public void addToPane(Pane parent) {
        parent.getChildren().add(this.deviceBlock);
    }
    
    // adds the capability to the device
    public void addCapability(Capability cap) {
        this.capabilities.add(cap);
    }
    
    // remove capability from the device
    public boolean removeCapability(Capability cap){
        return this.capabilities.remove(cap);
    }

    @Override
    public String toString() {
        return (this.getDeviceName(Locale.US) + " (" + this.getDeviceID() + ")");
    }

    // returns capability block
    public Block getCapabilityBlock(String capId) {
        for (Capability capabilitie : capabilities) {
            if (capabilitie.getCapID().equals(capId)) {
                return capabilitie.cloneCapability().getBlock();
            }
        }

        return null;
    }

}
