# Rick-And-Morty

## ScreenShots
![cover](https://github.com/umutsaydam/AlarmApp/assets/69711134/9d549919-972f-4969-86d0-57d7532d81de)

<div>
<h3><a href="#uygulama-icerik">TR</a></h3>
<h3><a href="#app-content">EN</a></h3>
</div>

## TR
### <p id="uygulama-icerik"></p>
Uygulama Özellikleri
 - İstenilen haftanın gününe ve saatine alarm kurmanızı sağlar.
 - Değiştirilebilir zil sesi, titreşim etkinleştirme gibi özellikler mevcut.
 - Zamanlayıcı ile geri sayım özelliği.
 - Cihaz diline göre Türkçe ve İngilizce desteği.
 - Kronometre ile zaman ölçümü

## Kullanılan Teknolojiler - Kütüphaneler
- Kotlin
- Coroutines - Main thread'leri bloklayıp uygulamaları yanıt vermemesine sebep olabilecek işlemleri asenkron bir şekilde arka planda yapmamızı sağlar.
- JetPack
    - LiveData - Verileri gözlemlenebilir olmasını sağlar.
    - Lifecycle 
    - ViewModel - MVVM mimaresinin bir parçası olan view model, mantıksal işlemlerin yürütüldüğü kısımdır.
    - Room - Temeli SQLite olan veri tabanı. 
    - Navigation - Fragment'lar arası dolaşım ve parametre gönderimi için kullanıldı.
    - Data Binding - XML layout'larda yer alan komponentlerin kod kısmından verimli bir şekilde erişebilmesini sağlar. 
- Material-Components - Material design komponentler (cardView gibi).
- Notification - Cihazda bildirim vermemizi sağlar.
- Service - Kurulan alarma göre tetiklenir veya zamanlayıcı/kronometre gibi hizmetlerin arka planda çalışmasını sağlar.

## <p id="app-content">EN</p>
App Features
  - Allows setting alarms for any day and time of the desired week.
  - Features like customizable ringtone and vibration are available.
  - Countdown feature with a timer.
  - Turkish and English language support depending on the device language.
  - Time measurement with a stopwatch.

## Tech Stack - Library
- Kotlin
- Coroutines - Allows us to perform operations asynchronously in the background, which may block main threads and cause applications to stop responding.
- JetPack
    - LiveData - It makes the data observable.
    - Lifecycle
    - ViewModel - The view model, which is a part of the MVVM architecture, is the part where logical operations are carried out.
    - Room - Database based on SQLite.
    - Navigation - Used for inter-fragment navigation and parameter sending.
    - Data Binding - It enables the components in XML layouts to be accessed efficiently from the code section.
- Material-Components - Material design components (like cardview).
- Notification - Enables displaying notifications on the device.
- Service - Triggered based on set alarms or enables the background operation of services like timers/stopwatches.

