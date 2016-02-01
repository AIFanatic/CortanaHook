# CortanaHook - The first Cortana interceptor without a middleman

## The protocol

By now there are already some resources online on how it all works, but let's go thru the basics and move on, since the protocol doesn't matter that much

Cortana firstly seems to do some sort of handshake to https://www.bing.com/threshold/xls.aspx

Text and speech are handled differently..

### Text request

```
HEADERS:

Host	www.bing.com
Connection	keep-alive
Accept	text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
User-Agent	Mozilla/5.0 (Linux; Android 4.4.4; GT-I9300 Build/KTU84Q) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36 Cortana/5.0.0.0 VersionCode/745 ROM/default
x-bm-devicescale	200.0
x-bm-cbt	752882728
timeoffset	480
x-bm-devicedimensionslogical	360x640
x-bm-mo	50502
x-bm-dateformat	M/d/yyyy
x-bm-theme	FFFFFF;08517b
x-bm-bandwidth	High
x-search-mobileclienttype	Hose
x-bm-deviceorientation	0
x-deviceid	93e5d81687258ee8
x-bm-dtz	480
x-search-rpstoken	t=EwAAAkF6BAAUWkziSC7RbDJKS1VkhugDegv7L0eAALVSUr/hFDvmkhdLgQS7UbnDl3UDO6dCystBs7MHdLfx29DH/6OV+3JJxuIK7FXHslGU9VUkpt+qtgOYtp5kbcE7KPBRAtUeMulepZTOqKFCaUaPUIkJa1Y8N5xV/rlBLaBkmvzZWCQ1BiT2ijRZaW8/6BgMXgk1KfsCCWhJ24E5A2YAAAgtvRAv7sejR1ABIkk9NkAUuMzgg+qjpbef5a4GYMwLiDFM17lyChNZQG2bVfWzxKrHGpSNNaJ2xUYz2Xzfly1tvYBUmNztgvkt8lJfZpmQZC/2GzxmjM0nA/Qo8A+x54W5OA90grX7GwhVfyfzj5LHzPS7RECstVpT3dG0cV9gP1lPuQgiyejrfaGDoFnGhUrqYXH3kVa5AOjlOt7ge1oDxs08l5ss7rnn9311utzEeCSQyhEmJHhgnVmLkQe8VUgBAVPFSVNUPbjLdi685PNfGUqE8Aviqp0gdYXXNQMzQX1CIVQAv+TfnrAPzFWcsKY/rPk6wiGZqykH0NcoLBlgVDsE7ZF7vKuh1x3WTnHSQt8YoLnergN+b7lSE1duwHxBxnBxd/D95OKKKAobpYoA+70AfyPVRNKzjnI9SB5KIZCYdXMCFPj7FsFUZgBSSO1fXb/4VVD58zcIeAE=&p=
x-cu-requestdata	{"CUServiceId":"FB31CC89-63D2-4296-A806-33DBA8DA56F2","ClientVersion":"3.0.150531","ConversationId":"a43998a2-96d1-7ed2-5792-d3ac87b42783","IsNewConversationId":false,"QFClientContextTaskFrame":{"Service":"IceWeatherAnswer"},"SpeechLanguage":"en-us"}
x-device-clientsession	367268ffbd3d47d1b3e0023ac75907eb
x-device-machineid	{5757B271-737F-4C21-96E2-09B4D90FA035}
x-bm-market	us
x-search-ig	a30c41cb9d68922299482fc155db3ed9
x-bm-devicedimensions	720x1280
x-search-appid	D41D8CD98F00B204E9800998ECF8427E09AA4958
x-search-uilang	en-us
x-search-clientid	3a7c4cc755726d83010c446154e26ce8
x-bm-userdisplayname	cortana
Accept-Encoding	gzip,deflate
Accept-Language	en-US
X-Requested-With	com.microsoft.cortana

URL: https://bing.com/search?input=1&formcode=WNSGPH&q=Hello&cc=us&setlang=en-us&mkt=en-us
```

Entering the URL into a search bar just brings you to bing but by using the apropriate headers you are now talking with cortana!

Note that from all those headers most of them are not needed, they seem to handle different colors and layouts for the best user interface.

### Speech Request

URL: https://www.bing.com/speech_render?speech=1&input=2&form=WNSBOX&cc=US&setlang=en-US

The user speech is streamed as a parameter, the headers are the same as text.

### Response

The response of both requests is nothing more than a bing page formatted to mobiles with some pretty interesting data.

Here is the interesting parts:
```html
<div class="b_ans">
  <div data-emot="CALM" data-emot-overridespeaking="1"></div>
  <div data-ssml="<speak version="1.0" xmlns="http://www.w3.org/2001/10/synthesis" xmlns:mstts="http://www.w3.org/2001/mstts" xml:lang="en-US"><mstts:audiosegment data="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">Hey, what can I do for you?</mstts:audiosegment></speak>"></div>
  <h2 class="b_anno b_anim">Hey, what can I do for you?</h2>
  <ul class="b_vList"></ul>
</div>
```

data-emot: Not sure what this does and did not play with it, for now

data-smml: This is where the magic happens, mstts:audiosegment holds the raw audio data (base64 and encrypted) that cortana will speak.

If this field is not specified Cortana can also speak regular text (not with voice emphasis though)

But the audiosegment is encrypted so what can I do?

Please refer to Project Oxford (Speech API) if you want to understand more about the text to speech and audiosegment decoding, Cortana uses exactly the same API's.

## Reverse engineering android apk

Cortana.apk is not encrypted so it can be easly decompiled. (root required in order to make changes)

### The node API

Cortana.apk comes with a node environment, more interestingly with a very raw API called "app_HalseyService"

Cortana fires up a node server at each shutdown of the app (yes, not at launch)

The app itself does some security measures on this folder and checks its integrity, so in order to change files you need to disable write permissions (with root browser or similar)

The node server only parses ssml and returns the proper audio, it doesn't seem to be able to make any callbacks to Cortana in order to change it's behavior.
```
/data/data/com.microsoft.cortana/app_HalseyService/data/HalseyService
```
#### HalseyService/index.js

By reading the javascript code we see that cortana uses express and middler to fire up a proxy and intercept some calls, the most interesting being "/cortanaTts"

At line 21 we see that it has a isDebugMode variable, changing it to 1 trows some interesting info to Logcat like device indentification, it also changes the server ip address to the device so we can make calls to it.

At the very last line we see "dssServer.listen(listeningPort, HTTP_LISTEN_ADDRESS);", viewing Logcat outputs we can see that node keeps changing the server port at each reboot, changing it to 9999 for example makes the node run always on the same port, much easier to debug.

#### HalseyService/api.js

The handler of the API..

Since I don't want to use middler or body-parser, I just changed the required fields to make it work.

In the next file cortanaTts.js we have a basic structure of the ssml format, changing the fields in api.js, line 68-70 makes it work:

```javascript
var ssml = "<speak version=\"1.0\" xmlns=\"http://www.w3.org/2001/10/synthesis\" xmlns:mstts=\"http://www.w3.org/2001/mstts\" xmlns:emo=\"http://www.w3.org/2009/10/emotionml\" xml:lang=\"%s\">%s<emo:emotion><emo:category name=\"CALM\" value=\"1.0\"/>Hello</emo:emotion></voice></speak>";
var appLanguage = "en-US";
var outputFormat = "riff-16khz-16bit-mono-pcm";
```

#### HalseyService/cortanaTts.js

Not much to do here, only adding a log so we can see the response output at the end of the function getWave "logger.log('cortanaHook: response', response.body);"

### How to call cortanaTts?

Use any tool, my favourite is Postman, make a POST request to http://ip-of-phone:9999/cortanaTts and see the output appear in Logcat

Note the output won't show in total at Logcat due to it's size.

### Sniffing requests

Using a middleman should be pretty easy right? Well Cortana has it's tricks, even if they are simple...

If you try to use a proxy on the phone it detects it and doesn't let you go forward, showing "proxy servers are not allowed at this time".

We could dig in the smali code and disable the warning but since the phone is rooted the easiest way to go is to use any proxy tool that works at root level (ProxyDroid)

This combination and a root ssl certificate (charles proxy) enables us to see and hijack all the communications between the app and bing.

## APK Decompiling, Security and Hooking

This is the main goal, be able to intercept what the client said and change Cortana's response accordingly

In the PoC code I use "findAndHookMethod" to intercept any "setText" calls that are being made to Cortana, this will give us the client text to speech

The next step is to hook "loadUrl" from "webView" in order to intercept the returned response.

Changing the url to something else fires another security measure that keeps refreshing the page if "bing.com..." or "about:blank" are not present.

Even though this can be disabled in smali, it's to painfull to keep trying changes, recompile, sign the apk and upload to the device.

For reference the security measure is implemented in com\microsoft\bing\dss\ae.java:line 28

```
if ((paramString != null) && (!paramString.equalsIgnoreCase("about:blank")))
{
  localBingWebView.loadUrl(String.format("javascript:if(!document.body.innerHTML || document.body.innerHTML.indexOf('%s') > -1 || document.body.innerHTML == '%s'){window.CortanaApp.reportErrorContent(document.body.innerHTML);}", new Object[] { str1, str2 }));
  localBingWebView.loadUrl("javascript:;setTimeout(function(){document.body.style.display = 'inline';setTimeout(function(){document.body.style.display = 'block';}, 200);}, 1000);");
}
```

Note: The code was decompiled from smali into java, the changes need to be done into smali, also Xposed Installer module is needed to update new apk's without a matching certificate.

By removing and installing the new APK you will have to setup/login again into bing and microsoft will send the APK signature to the servers, returning false if it doesn't match.

This signature can be hijacked using a middleman attack and changing the signature back to the original one, or it can be disabled in smali.

The solution is to inject javascript code into the url to suit our needs using the "evaluateJavascript" method.

## Downsides

So now we can intercept user text to speech and change the answer or even call an external function.

But by changing Cortana's answer from audiosegment to simple text we will loose one of Cortana's best features (IMO), voice emphasis.

This is lost because from my understanding the emphasis is done on microsoft side, when the user first sends TTS, MS replies with the audio bytes itself not text for Cortana to process.

## PoC

The code provided is just a proof of concept, in future releases I expect to provide a more elegant way, calling scripts to handle everything.


Microsoft (Rob), please release an API for Android Cortana similar to the one in Windows/Phone and is there a chance for Linux to?


This repo is for educational purposes only
