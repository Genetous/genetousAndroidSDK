![Genetous](logo.png "Genetous")

# **Genetous Android SDK**

**Genetous Android SDK** is integrated with Genetous Low Code Platform.

**Genetous Android SDK** allows you to exchange data with services without the need for another tool.

## **Genetous Android SDK installation**

### **In setting.gradle**

```java
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

### **or in project build.gradle**

```java
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

### **After that in app build.gradle**

```java
dependencies {
    implementation 'com.github.Genetous.genetousAndroidSDK:genetous_http:v1.0.0'
}
```

### **In Android Manifest**

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest>

    <application>
        ...
        <meta-data android:name="applicationId" android:value="your applicationId" />
        <meta-data android:name="organizationId" android:value="your organizationId" />
    </application>

</manifest>
```

Contact us for issues with this SDK!

<https://www.genetous.com>

<info@genetous.com>

### All rights of this SDK reseverd to **Genetous BaaS Platform.**
