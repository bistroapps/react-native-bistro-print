<p align="center">
  <a href="http://nestjs.com/" target="blank"><img src="https://avatars.githubusercontent.com/u/66649275?s=400&u=13451b2fdf98f8283b669700e078f78ddf2c1812&v=4" width="200" alt="Nest Logo" /></a>
</p>

#Lib Service Printer

## Description

[Print-Bistro](https://github.com/bistroapps/print-bistro) framework React native starter repository.

## Installation

```bash
yarn add https://github.com/bistroapps/print-bistro.git
```

## Use the app



  Import the `printBistro` component from `print-bistro/js/NativePrintBistro` and use it like so:

  ```jsx
    import React, { useEffect } from 'react';
    import printBistro from 'print-bistro/js/NativePrintBistro'

    const App = () => {
      
     async function onPrint(){

        const bodyPrint = JSON.stringify({
              print: [
                {
                  type: 'text',
                  text: 'Complemento',
                  bold: false,
                  align: 'Center',
                  underscore: false,
                  doubleWidth: false,
                  doubleHeight: false,
                  reverse: false,
                },
              ],
            });

        await printBistro.print(
          'codePrint',
           bodyPrint
        )
      }
       
      ...
    }
  ```


  Methods:
  ```ts
    printBistro.isAppInstalled(packageName: string): Promise<boolean>
    
    printBistro.startAppByPackageName(packageName: string): Promise<boolean | boolean>;
    
    printBistro.getPrinters(): Promise<string>;
    
    printBistro.print(printerCode: string, jsonInput: string): Promise<string>;
  ```


## Support
  not support ios

## Stay in touch

 - Author - [Francy Helder](https://github.com/HelderSi)
 - Author - [Douglas Froes](https://github.com/DouglasFroes)
<!--
- Website - [https://nestjs.com](https://nestjs.com/)
- Twitter - [@nestframework](https://twitter.com/nestframework) -->

## License

Software Bistro is [MIT licensed](LICENSE).

