# 🚀 HotPay Java SDK

[![Java Version](https://img.shields.io/badge/Java-11%2B-blue.svg)](https://adoptium.net/)
[![Build Status](https://img.shields.io/github/actions/workflow/status/XeNikk/hotpay-java-sdk/build.yml?branch=master)](https://github.com/XeNikk/hotpay-java-sdk/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

Profesjonalna, lekka i całkowicie niezależna (zero-dependency) biblioteka do integracji z bramką płatności **HotPay.pl** w aplikacjach opartych na języku Java.

Zaprojektowana z myślą o czystej architekturze, bezpieczeństwie (weryfikacja adresów IP i podpisów cyfrowych SHA-256) oraz łatwości integracji z dowolnym frameworkiem webowym (Spring Boot, Quarkus, Micronaut, Ktor lub czysty serwer HTTP).

## ✨ Główne cechy
* **Zero Zależności (Zero-dependency):** Biblioteka korzysta wyłącznie z wbudowanych mechanizmów Javy (kryptografia, operacje na ciągach znaków). Nie zanieczyszcza Twojego projektu bibliotekami takimi jak Apache, OkHttp czy Jackson.
* **Bezpieczeństwo by-design:** Wbudowana biała lista oficjalnych adresów IP serwerów HotPay oraz odporna na błędy weryfikacja algorytmu HASH.
* **Niezmienność (Immutability):** Konfiguracja i obiekty żądań oparte na wzorcu Builder chronią przed przypadkową modyfikacją stanu aplikacji.
* **Dedykowane Wyjątki:** Łatwa obsługa błędów dzięki klasom takim jak `SignatureVerificationException` czy `UnauthorizedIpException`.

---

## 📦 Wymagania i Instalacja

* **Java:** Wersja 11 lub nowsza.
* **System budowania:** Maven lub Gradle.

Aby użyć biblioteki w swoim projekcie, sklonuj repozytorium i zbuduj je lokalnie (lub pobierz wydanie, jeśli zostało opublikowane w rejestrze pakietów):

**Maven (`pom.xml`):**
```xml
<dependency>
    <groupId>pl.hotpay</groupId>
    <artifactId>hotpay-java-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## 🚀 Szybki Start

### 1. Inicjalizacja Klienta

Najlepiej utworzyć instancję klienta jako Singleton (np. jako `@Bean` w Springu) na starcie aplikacji.

```java
import pl.hotpay.sdk.HotPayClient;
import pl.hotpay.sdk.config.HotPayConfig;

// Wprowadź dane ze swojego panelu konfiguracyjnego HotPay
HotPayConfig config = new HotPayConfig("TWÓJ_SEKRET", "HASŁO_Z_USTAWIEN");
HotPayClient client = new HotPayClient(config);
```

### 2. Generowanie płatności (Link URL)

Szybki i wygodny sposób na wygenerowanie bezpośredniego linku do bramki płatności, idealny do przesłania użytkownikowi na czacie lub w oknie gry.

```java
import pl.hotpay.sdk.model.PaymentRequest;

PaymentRequest request = new PaymentRequest.Builder()
    .amount("25.50")
    .serviceName("Twój produkt")
    .redirectUrl("https://example.com/sukces")
    .orderId("ID_Zamóienia")
    .email("kontakt@example.com") // Opcjonalne
    .build();

String paymentUrl = client.generatePaymentUrl(request);
System.out.println("Wygenerowany adres płatności: " + paymentUrl);
```

### 3. Odbieranie powiadomień Webhook (IPN)

Twoja aplikacja musi posiadać publiczny endpoint (np. `/webhook`), który odbierze żądanie `POST` typu `application/x-www-form-urlencoded` od serwerów HotPay.

Przykład abstrakcyjnej obsługi w dowolnym frameworku:

```java
import pl.hotpay.sdk.model.HotPayNotification;
import pl.hotpay.sdk.exceptions.UnauthorizedIpException;
import pl.hotpay.sdk.exceptions.SignatureVerificationException;
import java.util.Map;

public void handleHotPayWebhook(Map<String, String> postData, String clientIp) {
    try {
        // 1. Mapowanie surowych danych POST do obiektu
        HotPayNotification notification = new HotPayNotification(postData);

        // 2. Walidacja bezpieczeństwa (IP + Cyfrowy Podpis HASH)
        client.verifyNotification(notification, clientIp);

        // 3. Logika biznesowa
        if (notification.isSuccess()) {
            System.out.println("Płatność zaakceptowana! Zamówienie: " + notification.getOrderId());
        } else if (notification.isPending()) {
            System.out.println("Czekamy na wpłatę. Zamówienie wisi w statusie oczekującym.");
        } else if (notification.isFailure()) {
            System.out.println("Płatność odrzucona.");
        }
    } catch (UnauthorizedIpException e) {
        System.err.println("Adres IP nie jest na dozwolonej liście: " + clientIp);
    } catch (SignatureVerificationException e) {
        System.err.println("Sygnatura notyfikacji jest niepoprawna.");
    }
}
```

---

## 📄 Licencja

Ten projekt jest licencjonowany na warunkach **MIT License**. Możesz go dowolnie używać w projektach komercyjnych i prywatnych.