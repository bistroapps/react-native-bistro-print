<p align="center">
  <a href="http://nestjs.com/" target="blank"><img src="https://avatars.githubusercontent.com/u/66649275?s=400&u=13451b2fdf98f8283b669700e078f78ddf2c1812&v=4" width="200" alt="Nest Logo" /></a>
</p>


## Description

[Kioskmode](https://github.com/softwarebistro/Kioskmode) framework React native starter repository.

## Installation

```bash
yarn add https://github.com/softwarebistro/Kioskmode.git
```

## Use the app



  Import the `kioskmode` component from `rtn-kioskmode/js/NativeKioskmode` and use it like so:

  ```jsx
    import React, { useEffect } from 'react';
    import kioskmode from 'rtn-kioskmode/js/NativeKioskmode'

    const App = () => {
   
        useEffect(()=>{
            kioskmode.startKioskMode()
        },[])
   
      ...
    }
  ```


  Methods:
  ```ts
    kioskmode.checkPermissions() //
    
    kioskmode.startKioskMode() // inita kiosk mode
    
    kioskmode.stopKioskMode() // 
    
    kioskmode.closeApp() //
  ```


## Support
  not support ios

## Stay in touch

 - Author - [Douglas Froes](https://github.com/DouglasFroes)
<!--
- Website - [https://nestjs.com](https://nestjs.com/)
- Twitter - [@nestframework](https://twitter.com/nestframework) -->

## License

Software Bistro is [MIT licensed](LICENSE).

