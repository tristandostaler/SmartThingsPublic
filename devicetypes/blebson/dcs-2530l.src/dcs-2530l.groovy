/**
 *  D-Link DCS-2530L v1.0.3
 *  Image Capture and Video Streaming courtesy Patrick Stuart (patrick@patrickstuart.com)
 *  
 *  Copyright 2015 blebson
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "DCS-2530L", author: "Ben Lebson", namespace: "blebson") {
		capability "Image Capture"
		capability "Sensor"
		capability "Switch"
        capability "Switch Level"
        capability "Refresh"
        capability "Notification"
        capability "Configuration"
		capability "Video Camera"
		capability "Video Capture"
        
		attribute "hubactionMode", "string"
        attribute "switch2", "string"
        attribute "switch3", "string"
        attribute "switch4", "string"
        attribute "switch5", "string"
        
        command "pirOn"
        command "pirOff"
        command "nvOn"
        command "nvOff"
        command "nvAuto"
        command "vrOn"
        command "vrOff"
        command "start"
		command "stop"
        command "vidOn"
        command "vidOff"
        command "configure"
	}

    preferences {
    
	}
    
    mappings {
    	path("/getInHomeURL") {
    		action:
    		[GET: "getInHomeURL"]
   		}
	}
    
	simulator {
    
	}

    tiles (scale: 2){
    multiAttributeTile(name: "videoPlayer", type: "videoPlayer", width: 6, height: 4) {
			tileAttribute("device.switch5", key: "CAMERA_STATUS") {
				attributeState("on", label: "Active", icon: "st.camera.dlink-indoor", action: "vidOff", backgroundColor: "#79b821", defaultState: true)
				attributeState("off", label: "Inactive", icon: "st.camera.dlink-indoor", action: "vidOn", backgroundColor: "#ffffff")
				attributeState("restarting", label: "Connecting", icon: "st.camera.dlink-indoor", backgroundColor: "#53a7c0")
				attributeState("unavailable", label: "Unavailable", icon: "st.camera.dlink-indoor", action: "refresh.refresh", backgroundColor: "#F22000")
			}

			tileAttribute("device.errorMessage", key: "CAMERA_ERROR_MESSAGE") {
				attributeState("errorMessage", label: "", value: "", defaultState: true)
			}

			tileAttribute("device.camera", key: "PRIMARY_CONTROL") {
				attributeState("on", label: "Active", icon: "st.camera.dlink-indoor", backgroundColor: "#79b821", defaultState: true)
				attributeState("off", label: "Inactive", icon: "st.camera.dlink-indoor", backgroundColor: "#ffffff")
				attributeState("restarting", label: "Connecting", icon: "st.camera.dlink-indoor", backgroundColor: "#53a7c0")
				attributeState("unavailable", label: "Unavailable", icon: "st.camera.dlink-indoor", backgroundColor: "#F22000")
			}

			tileAttribute("device.startLive", key: "START_LIVE") {
				attributeState("live", action: "start", defaultState: true)
			}

			tileAttribute("device.stream", key: "STREAM_URL") {
				attributeState("activeURL", defaultState: true)
			}
			tileAttribute("device.betaLogo", key: "BETA_LOGO") {
				attributeState("betaLogo", label: "", value: "", defaultState: true)
			}
            }
    	carouselTile("cameraDetails", "device.image", width: 3, height: 2) { }
        standardTile("take", "device.image", width: 2, height: 2, canChangeIcon: false, inactiveLabel: true, canChangeBackground: false) {
            state "take", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
            state "taking", label:'Taking', action: "", icon: "st.camera.take-photo", backgroundColor: "#53a7c0"
            state "image", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
        }

        standardTile("refresh", "command.refresh", inactiveLabel: false) {
        	state "default", label:'refresh', action:"refresh.refresh", icon:"st.secondary.refresh-icon"        
    	}
        standardTile("motion", "device.switch", width: 2, height: 2, canChangeIcon: false) {
			state "off", label: 'Motion Off', action: "switch.on", icon: "st.motion.motion.inactive", backgroundColor: "#ccffcc", nextState: "toggle"
            state "toggle", label:'toggle', action: "", icon: "st.motion.motion.inactive", backgroundColor: "#53a7c0"
			state "on", label: 'Motion On', action: "switch.off", icon: "st.motion.motion.active", backgroundColor: "#EE0000", nextState: "toggle"            
		}
         standardTile("PIR", "device.switch2", width: 2, height: 2, canChangeIcon: false) {
			state "off", label: 'PIR Off', action: "pirOn", icon: "st.custom.buttons.rec", backgroundColor: "#ccffcc", nextState: "toggle"
            state "toggle", label:'toggle', action: "", icon: "st.motion.buttons.rec", backgroundColor: "#53a7c0"
			state "on", label: 'PIR On', action: "pirOff", icon: "st.custom.buttons.rec", backgroundColor: "#EE0000", nextState: "toggle"
		}
        standardTile("nightVision", "device.switch3", width: 2, height: 2, canChangeIcon: false) {
			state "off", label: 'NV Off', action: "nvAuto", icon: "st.Weather.weather14", backgroundColor: "#ffff00", nextState: "toggle"
            state "toggle", label:'toggle', action: "", icon: "st.motion.motion.inactive", backgroundColor: "#53a7c0"
			state "on", label: 'NV On', action: "nvOff", icon: "st.Weather.weather4", backgroundColor: "#4169E1", nextState: "toggle"  
            state "auto", label: 'NV Auto', action: "nvOn", icon: "st.motion.motion.active", backgroundColor: "#ccffcc", nextState: "toggle"  
		}
        standardTile("Video", "device.switch4", width: 2, height: 2, canChangeIcon: false) {
			state "off", label: 'Video Off', action: "vrOn", icon: "st.Entertainment.entertainment9", backgroundColor: "#ccffcc", nextState: "toggle"
            state "toggle", label:'toggle', action: "", icon: "st.Entertainment.entertainment9", backgroundColor: "#53a7c0"
			state "on", label: 'Video On', action: "vrOff", icon: "st.Entertainment.entertainment9", backgroundColor: "#EE0000", nextState: "toggle"
		}
        valueTile("Sensitivity", "device.level", inactiveLabel: false){
        	state "default", label:'${currentValue}%', unit:"%"
        }
       controlTile("levelSliderControl", "device.level", "slider", height: 1, width: 5, inactiveLabel: false, range:"(0..100)") {
            state "level", action:"switch level.setLevel"
        }
        standardTile("videoStart", "device.image", inactiveLabel: false) {
        	state "start", action:"start", icon:"st.Entertainment.entertainment11"        
    	}
        
        main "motion"
        details(["videoPlayer","cameraDetails", "take","refresh","videoStart","levelSliderControl","Sensitivity","motion","PIR","nightVision", "Video"])
    }
}

def parse(String description) {
    log.debug "Parsing '${description}'"
    if( description != "updated" ){
    def map = [:]
	def retResult = []
	def descMap = parseDescriptionAsMap(description)
    def msg = parseLanMessage(description)
    //log.debug "status ${msg.status}"
    //log.debug "data ${msg.data}"
    
	//Image
	if(descMap["tempImageKey"] ) {
				try {
					storeTemporaryImage(descMap.tempImageKey, getPictureName())
				} catch(Exception e) {
					log.error e
				}
	}      
    else if (descMap["headers"] && descMap["body"]){
    	def body = new String(descMap["body"].decodeBase64())
        log.debug "Body: ${body}"
    }
        
    if (msg.body) {
    
    //log.debug "Video Recording Enabled: ${msg.body.contains("<record>\n<enable>1</enable>")}"
    //log.debug "Video Recording Disabled: ${msg.body.contains("<record>\n<enable>0</enable>")}"
    //log.debug "Motion Enabled: ${msg.body.contains("enable=yes")}"
    //log.debug "Motion Disabled: ${msg.body.contains("enable=no")}"
    //log.debug "PIR Enabled: ${msg.body.contains("pir=yes")}"
    //log.debug "PIR Disabled: ${msg.body.contains("pir=no")}"
    
        if (msg.body.contains("enable=yes")) {
            log.debug "Motion is on"
            sendEvent(name: "switch", value: "on");
        }
        else if (msg.body.contains("enable=no")) {
            log.debug "Motion is off"
            sendEvent(name: "switch", value: "off");
        }
        if (msg.body.contains("pir=yes"))
        {
        	log.debug "PIR is on"
        	sendEvent(name: "switch2", value: "on");
        }
        else if (msg.body.contains("pir=no"))
        {
        	log.debug "PIR is off"
        	sendEvent(name: "switch2", value: "off");
        }
        if(msg.body.contains("sensitivity="))
        {
        	//log.debug msg.body        
        	String[] lines = msg.body.split( '\n' )
        	//log.debug lines[2]
            String[] sensitivity = lines[2].split( '=' )
            //log.debug sensitivity[1]
            int[] senseValue = sensitivity[1].toInteger()
            //log.debug senseValue
            
            sendEvent(name: "level",  value: "${senseValue[0]}")
            //sendEvent(name: "switch.setLevel", value: "${senseValue}")
        }       
        
        if (msg.body.contains( "mode=night")) {
            log.debug "Night Vision is on"
            sendEvent(name: "switch3", value: "on");
        }
        else if (msg.body.contains("mode=day")) {
            log.debug "Night Vision is off"
            sendEvent(name: "switch3", value: "off");
        }
        else if (msg.body.contains("mode=auto")) {
            log.debug "Night Vision is auto"
            sendEvent(name: "switch3", value: "auto");
        }
        
        if (msg.body.contains("<record>\n<enable>0</enable>")) {
        	log.debug "Video Recording Disabled"
            sendEvent(name: "switch4", value: "off");
        }
        else if (msg.body.contains("<record>\n<enable>1</enable>")) {
        	log.debug "Video Recording Enabled"
            sendEvent(name: "switch4", value: "on");
        }
    }    
    device.deviceNetworkId = "ID_WILL_BE_CHANGED_AT_RUNTIME_" + (Math.abs(new Random().nextInt()) % 99999 + 1)
    }
}

// handle commands
def take() {
	def userpassascii = "${state.cameraUser}:${state.cameraPassword}"
	def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    def host = state.cameraIP 
    def hosthex = convertIPtoHex(host)
    def porthex = convertPortToHex(state.cameraPort)
    device.deviceNetworkId = "$hosthex:$porthex" 
    
    log.debug "The device id configured is: $device.deviceNetworkId"
    
    def path = "/image/jpeg.cgi" 
    log.debug "path is: $path"
    
    def headers = [:] 
    headers.put("HOST", "$host:$state.cameraPort")
   	headers.put("Authorization", userpass)
    
    log.debug "The Header is $headers"
    
    def method = "GET"    
    
    log.debug "The method is $method"
    
    try {
    def hubAction = new physicalgraph.device.HubAction(
    	method: method,
    	path: path,
    	headers: headers
        )
        	
    hubAction.options = [outputMsgToS3:true]
    log.debug hubAction
    hubAction
    }
    catch (Exception e) {
    	log.debug "Hit Exception $e on $hubAction"
    }
    
}

def motionCmd(int motion, String attr)
{
	def userpassascii = "${state.cameraUser}:${state.cameraPassword}"
	def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    def host = state.cameraIP 
    def hosthex = convertIPtoHex(host)
    def porthex = convertPortToHex(state.cameraPort)
    device.deviceNetworkId = "$hosthex:$porthex" 
    
    log.debug "The device id configured is: $device.deviceNetworkId"
    
    def headers = [:] 
    headers.put("HOST", "$host:$state.cameraPort")
    headers.put("Authorization", userpass)
    
    log.debug "The Header is $headers"
    
    if (motion == 1){
 def path = "/config/motion.cgi?${attr}=yes"
 log.debug "path is: $path"
  try {
    def hubAction = new physicalgraph.device.HubAction(
    	method: "GET",
    	path: path,
    	headers: headers
        )
        	
   
    log.debug hubAction
    return hubAction
    
    }
    catch (Exception e) {
    	log.debug "Hit Exception $e on $hubAction"
    }
  }
  else
  {
  def path = "/config/motion.cgi?${attr}=no"
 log.debug "path is: $path"  
  try {
    def hubAction = new physicalgraph.device.HubAction(
    	method: "GET",
    	path: path,
    	headers: headers        
        )
     
    log.debug hubAction
    return hubAction
    
    }
    catch (Exception e) {
    	log.debug "Hit Exception $e on $hubAction"
    }
  
 
}
}
def sensitivityCmd(int percent)
{
	def userpassascii = "${state.cameraUser}:${state.cameraPassword}"
	def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    def host = state.cameraIP 
    def hosthex = convertIPtoHex(host)
    def porthex = convertPortToHex(state.cameraPort)
    device.deviceNetworkId = "$hosthex:$porthex" 
    
    log.debug "The device id configured is: $device.deviceNetworkId"
    
    def pir_percent = []
        
    if (percent <= 33)
    {
    pir_percent = 0
    }
    else if (percent > 33 && percent < 66)
    {
    pir_percent = 50
    }
    else if (percent >= 66)
    {
    pir_percent = 100
    }
    
    log.debug "Sensitivity is ${percent} and PIR Sensitivity is ${pir_percent}"
    
    def path = "/config/motion.cgi?sensitivity=${percent}&pir_sensitivity=${pir_percent}"
    log.debug "path is: $path"
        
    def headers = [:] 
    headers.put("HOST", "$host:$state.cameraPort")
    headers.put("Authorization", userpass)
    
    log.debug "The Header is $headers"
   
  try {
    def hubAction = new physicalgraph.device.HubAction(
    	method: "GET",
    	path: path,
    	headers: headers
        )
        	
   
    log.debug hubAction
    return hubAction
    
    }
    catch (Exception e) {
    	log.debug "Hit Exception $e on $hubAction"
    }
  
}

def nightCmd(String attr)
{
	def userpassascii = "${state.cameraUser}:${state.cameraPassword}"
	def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    def host = state.cameraIP 
    def hosthex = convertIPtoHex(host)
    def porthex = convertPortToHex(state.cameraPort)
    device.deviceNetworkId = "$hosthex:$porthex" 
    
    log.debug "The device id configured is: $device.deviceNetworkId"
    
    def headers = [:] 
    headers.put("HOST", "$host:$state.cameraPort")
    headers.put("Authorization", userpass)
    
    log.debug "The Header is $headers"
    
 def path = "/config/icr.cgi?mode=${attr}"
 log.debug "path is: $path"
  try {
    def hubAction = new physicalgraph.device.HubAction(
    	method: "GET",
    	path: path,
    	headers: headers
        )
        	
   
    log.debug hubAction
    return hubAction
    
    }
    catch (Exception e) {
    	log.debug "Hit Exception $e on $hubAction"
    }
   
}

def videoCmd(int attr)
{
	def userpassascii = "${state.cameraUser}:${state.cameraPassword}"
	def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    def host = state.cameraIP 
    def hosthex = convertIPtoHex(host)
    def porthex = convertPortToHex(state.cameraPort)
    device.deviceNetworkId = "$hosthex:$porthex" 
    
    log.debug "The device id configured is: $device.deviceNetworkId"
    
    def headers = [:] 
    headers.put("HOST", "$host:$state.cameraPort")
    headers.put("Authorization", userpass)
    
    log.debug "The Header is $headers"
    
 def path = "cgi/admin/recorder.cgi?recordEnable=${attr}"
 log.debug "path is: $path"
  try {
    def hubAction = new physicalgraph.device.HubAction(
    	method: "GET",
    	path: path,
    	headers: headers
        )
        	
   
    log.debug hubAction
    return hubAction
    
    }
    catch (Exception e) {
    	log.debug "Hit Exception $e on $hubAction"
    }  
}

def parseDescriptionAsMap(description) {
	description.split(",").inject([:]) { map, param ->
		def nameAndValue = param.split(":")
		map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
	}
}

private getPictureName() {
	def pictureUuid = java.util.UUID.randomUUID().toString().replaceAll('-', '')
    log.debug pictureUuid
    def picName = device.deviceNetworkId.replaceAll(':', '') + "_$pictureUuid" + ".jpg"
	return picName
}

private String convertIPtoHex(ipAddress) { 
    String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02x', it.toInteger() ) }.join()
    log.debug "IP address entered is $ipAddress and the converted hex code is $hex"
    return hex

}

private String convertPortToHex(port) {
	String hexport = port.toString().format( '%04x', port.toInteger() )
    log.debug hexport
    return hexport
}

private Integer convertHexToInt(hex) {
	Integer.parseInt(hex,16)
}


private String convertHexToIP(hex) {
	log.debug("Convert hex to ip: $hex") 
	[convertHexToInt(hex[0..1]),convertHexToInt(hex[2..3]),convertHexToInt(hex[4..5]),convertHexToInt(hex[6..7])].join(".")
}

private getHostAddress() {
    def parts = device.deviceNetworkId.split(":")
    log.debug device.deviceNetworkId
    def ip = convertHexToIP(parts[0])
    def port = convertHexToInt(parts[1])
    return ip + ":" + port
}


def on() {
	log.debug "Enabling motion detection"
    return motionCmd(1, "enable")    
    
}

def off() {
	log.debug "Disabling motion detection"
    return motionCmd(0, "enable")    
    
}
def pirOn() {
	log.debug "Enabling PIR Sensor"
    return motionCmd(1, "pir")  
    
}

def pirOff() {
	log.debug "Disabling PIR Sensor"
    return motionCmd(0, "pir") 
    
}

def setLevel(percent) {
	log.debug "Executing 'setLevel'"
	return sensitivityCmd(percent)	
    
}
def nvOn() {
	log.debug "Enabling Night Vision"
    return nightCmd("night")   
    
}

def nvOff() {
	log.debug "Disabling Night Vision"
    return nightCmd("day")    
    
}

def nvAuto() {
	log.debug "Automatic Night Vision"
    return nightCmd("auto")    
    
}

def vrOn() {
	log.debug "Video Recording On"
    return videoCmd(1) 
}

def vrOff() {
	log.debug "Video Recording Off"
    return videoCmd(0) 
}

def refresh(){

	log.debug "Refresh"
	def userpassascii = "${state.cameraUser}:${state.cameraPassword}"
	def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    def host = state.cameraIP 
    def hosthex = convertIPtoHex(host)
    def porthex = convertPortToHex(state.cameraPort)
    device.deviceNetworkId = "$hosthex:$porthex" 
    
    log.debug "The device id configured is: $device.deviceNetworkId"
    def path = "/config/motion.cgi"
    log.debug "path is: $path"
    
    def headers = [:] 
    headers.put("HOST", "$host:$state.cameraPort")
    headers.put("Authorization", userpass)
    
    log.debug "The Header is $headers"
   
  try {
    def hubAction = new physicalgraph.device.HubAction(
    	method: "GET",
    	path: path,
    	headers: headers
        )
        	
   
    log.debug hubAction
    return hubAction
    
    }
    catch (Exception e) {
    	log.debug "Hit Exception $e on $hubAction"
    }
  
  
}

def start() {
	log.trace "start()"
	def dataLiveVideo = [
		OutHomeURL  : "http://${state.cameraUser}:${state.cameraPassword}@${state.videoIP}:${state.videoPort}/video/mjpg.cgi",
		InHomeURL   : "http://${state.cameraUser}:${state.cameraPassword}@${state.videoIP}:${state.videoPort}/video/mjpg.cgi",
		ThumbnailURL: "http://cdn.device-icons.smartthings.com/camera/dlink-indoor@2x.png",
		cookie      : [key: "key", value: "value"]
	]

	def event = [
		name           : "stream",
		value          : groovy.json.JsonOutput.toJson(dataLiveVideo).toString(),
		data		   : groovy.json.JsonOutput.toJson(dataLiveVideo),
		descriptionText: "Starting the livestream",
		eventType      : "VIDEO",
		displayed      : false,
		isStateChange  : true
	]
	sendEvent(event)
}

def stop() {
	log.trace "stop()"
}

def vidOn() {
	log.trace "on()"
	// no-op
}

def vidOff() {
	log.trace "off()"
	// no-op
}

def installed(){
	configure()
}

def updated(){
	configure()
}

def configure(){
    sendEvent(name:"switch5", value:"on") 
    state.cameraIP = parent.state.CameraIP
    state.cameraPort = parent.state.CameraPort
    state.videoIP = parent.state.VideoIP
    state.videoPort = parent.state.VideoPort
    state.cameraUser = parent.state.CameraUser
    state.cameraPassword = parent.state.CameraPassword 
}

def getInHomeURL() {
   [InHomeURL: "http://${state.cameraUser}:${state.cameraPassword}@${state.videoIP}:${state.videoPort}/video/mjpg.cgi"]
}