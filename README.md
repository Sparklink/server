# BarcodeAPI.org

The BarcodeAPI.org web server was designed to provide an easy to use barcode generation API via the HTTP protocol for use in external / mobile applications
where generation libraries might not exist or resources limited;
the WebUI is also designed to be responsive such that users may generate barcodes to be scanned directly from their web browser or download the barcodes after testing them out.

Every customer of ours has their own barcode server hosted on google cloud.
If the base url or login credentials are missing, you can contact R&D.

## Web Server

The server comes with a minimal set of static HTML and Javascript files that allow users to generate barcodes in their web browser with ease.
The UI allows users to quickly download, print, or copy the images for use in other applications.

### Print multiple barcodes
You can redirect a user to the multi web page.
When passing parameters, the user will get to see all the barcodes that you have passed to it.
All the user needs to do is press the print button.

Example uri: `<baseurl>/multi.html?QRCode/myQrCode&auto/aSecondCode`

## API Server

The server will generate a barcode for any content passed to the `/api` endpoint.
This can be done using a web browser, fetched through a user script, or even simply with cURL.
The format of this endpoint is as follows `<baseurl>/api/<code-type>/<barcode>`;

```
curl <baseurl>/api/QRCode/A_Barcode > gen.png
```

### Supported Code Type

A list of all supported barcodes types is available by calling the `/types/` endpoint.
This will be a JSON Array containing details for each type including a regex format to prevalidate the date before a request.

Specific barcode types may be requested by adding the type string in the request URL:

```
curl <baseurl>/api/128/abc123
curl <baseurl>/api/qr/abc123
```

### Automatic Code Type Detection

When calling the API endpoint without specifying an eplitit code type the server will make its best judgement as to which code type will be best suited for the supplied data.

```
curl <baseurl>/api/auto/abc123
```

## Bulk server
Receive a json containing all the qr code images base64 encoded. Add multiple lines for multiple qr codes.
The bulk server can interpret both csv and json input.
The correct Content-Type header will need to be set.
```
curl -H "Content-Type: text/csv" --request POST --data "test,auto" <baseurl>/bulk/ -L -O -J
```

```
curl -H "Content-Type: application/json" --request POST --data '[{"data": "test", type: "auto"}]' <baseurl>/bulk/ -L -O -J
```

Example response:
```json
{
    "barcodes": [
        {
            "data": "test",
            "type": "Code128",
            "barcode": "iVBORw0KGgoAAAANSUhEUgAAAPgAAACWAQAAAADbVHiMAAAACXBIWXMAABcSAAAXEgFnn9JSAAAAEnRFWHRTb2Z0d2FyZQBCYXJjb2RlNEryjnYuAAAAoUlEQVR4Xu3RQQrCMBCF4YDSbKS9gblClxVK3mG8huDCRc8luPYMgQrdVqTUghC7nRECQkGQN5DN/0FIGBNjPDnc+5Wr/LE18N7sg70eXNidZzN0Op1Op9PpdDqdTqfT/8JTs6xP84Eoy3pfb3MrivJyk6V8elxGiKK8uwGiKA9ZAVG02wai6PtjB1H0+4akP8u1Tf3vVedFI8p3+/ucX/sbOxWMgntIk+8AAAAASUVORK5CYII=",
            "nice": "test"
        }
    ]
}
```

You can also retrieve the images zipped in a file by providing the "Accept: application/zip" header.
```
curl -H "Accept: application/zip" -H "Content-Type: text/csv" --request POST --data "test,auto" <baseurl>/bulk/ -L -O -J
```

**NOTE:** The url needs to end with `/bulk/`, including the last slash. I don't know why, but otherwise it breaks.

## Third-Party

BarcodeAPI.org is only made possible with the use of third-party software.

**Jetty, [Apache 2.0](https://www.eclipse.org/jetty/licenses.html)**

* The BarcodeAPI server was built around the [Jetty](https://www.eclipse.org/jetty/) web server framework.

**Barcode4J, [Apache 2.0](http://barcode4j.sourceforge.net/#Introduction)**

* [Barcode4J](http://barcode4j.sourceforge.net/) is an open source barcode generator; it is used for the generation of the following code types:

**ZXing, [Apache 2.0](https://github.com/zxing/zxing/blob/master/LICENSE)**

* [ZXing](https://github.com/zxing/zxing/) is a barcode processing library that makes QR code generation possible.

## License

```text
Copyright 2020 BarcodeAPI.org

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

