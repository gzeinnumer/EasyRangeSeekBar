
<p align="center">
  <img src="https://github.com/gzeinnumer/MyLibUtils/blob/master/preview/bg.jpg"/>
</p>

<h1 align="center">
    EasyRangeSeekBar
</h1>

<p align="center">
    <a><img src="https://img.shields.io/badge/Version-1.0.0-brightgreen.svg?style=flat"></a>
    <a><img src="https://img.shields.io/badge/ID-gzeinnumer-blue.svg?style=flat"></a>
    <a><img src="https://img.shields.io/badge/Java-Suport-green?logo=java&style=flat"></a>
    <a><img src="https://img.shields.io/badge/kotlin-Suport-green?logo=kotlin&style=flat"></a>
    <a href="https://github.com/gzeinnumer"><img src="https://img.shields.io/github/followers/gzeinnumer?label=follow&style=social"></a>
    <br>
    <p>Simple Range SeekBar.</p>
</p>

---
# Content List
* [Download](#download)
* [Feature List](#feature-list)
* [Tech stack and 3rd library](#tech-stack-and-3rd-library)
* [Usage](#usage)
* [Example Code/App](#example-codeapp)
* [Version](#version)
* [Contribution](#contribution)

---
# Download
Add maven `jitpack.io` and `dependencies` in `build.gradle (Project)` :
```gradle
// build.gradle project
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

// build.gradle app/module
dependencies {
  ...
  implementation 'com.github.gzeinnumer:EasyRangeSeekBar:version'
}
```

---
# Feature List
- [x] []()

---
# Tech stack and 3rd library
- []()

---
# Usage

### **SeekBar**
in `xml`
```xml
<com.gzeinnumer.ers.RangeSeekBar
    android:id="@+id/rsb"
    android:layout_width="250dp"
    android:layout_height="wrap_content" />
```
in activity
```java
RangeSeekBar rangeSeekBar = findViewById(R.id.rsb);
rangeSeekBar.setMax(500);
rangeSeekBar.setListener(new ListenerSeekBar() {
    @Override
    public void valueChanged(RangeSeekBar rangeSeekBar, float currentValue) {

    }
});
```
<p align="center">
  <img src="https://github.com/gzeinnumer/EasyRangeSeekBar/blob/remove-step-v2/preview/example4.jpg" width="400"/>
</p>

---

### **SeekBar Customize**

<p align="center">
  <img src="https://github.com/gzeinnumer/EasyRangeSeekBar/blob/remove-step-v2/preview/example3.jpg" width="400"/>
</p>

#
#### XML

**1. Bubble**
- `app:rsb_showBubble="true"`. Show or hide Bubble.

**2. TopText**
- `app:rsb_topTextVisible="true"`. Show or hide TopText.
- `app:rsb_topTextSize="16sp`. TopText size.
- `app:rsb_textFollowDot="true"`. Text follow dot.
- `app:rsb_textFollowRegionColor="true"`. Text color follow left/right color.

**3. ProgressBar**
- `app:rsb_barHeight="10dp"`. Bar Height.
- `app:rsb_barBackgroundColor="@color/colorPrimary"`. Default ProgressBar background.
- `app:rsb_leftColor="@color/colorAccent"`. Color Start ProgressBar.
- `app:rsb_rightColor="@color/colorPrimary"`. Color End ProgressBar.
- `app:rsb_smallDot="true"`. Dot Height = ProgressBarHeight.

**4. BottomText**
- `app:rsb_bottomTextVisible="true"`. Show or hide BottomText.
- `app:rsb_bottomTextSize="16sp`. BottomText size.

#
#### Programmatically

```java
RangeSeekBar rangeSeekBar = findViewById(R.id.rsb);
```

**1. Bubble**
- `soon`

**2. TopText**
- `rangeSeekBar.setMin(100);`. MinValue ProgressBar.
- `rangeSeekBar.setMax(500);`. MinValue ProgressBar.
- `rangeSeekBar.setCurrentValue(250);`. Current Value ProgressBar. Trigger with `Onclick`.
- Custom Text Format
```java
rangeSeekBar.setTextFormatter(new TextFormatterSeekBar() {
    @Override
    public String format(float value) {
        return String.format("Rp. %d", (int) value);
    }
});
```

**3. ProgressBar**
- `soon`

**4. BottomText**
- `rangeSeekBar.setTextMin("Min\nvalue");`. Min text description.
- `rangeSeekBar.setTextMan("Max\nvalue");`. Man text description.

---

### **Preview Customize**

- Example 1

<p align="center">
  <img src="https://github.com/gzeinnumer/EasyRangeSeekBar/blob/remove-step-v2/preview/example5.jpg" width="400"/>
</p>

```xml
<com.gzeinnumer.ers.RangeSeekBar
    android:id="@+id/rsb"
    android:layout_width="250dp"
    android:layout_height="wrap_content"
    app:rsb_leftColor="@color/colorLeft" />
```
```java
final RangeSeekBar rangeSeekBar = findViewById(R.id.rsb);
rangeSeekBar.setMin(100);
rangeSeekBar.setMax(500);
rangeSeekBar.setCurrentValue(250);
rangeSeekBar.setTextMax("max\nvalue");
rangeSeekBar.setTextMin("min\nvalue");
rangeSeekBar.setTextFormatter(new TextFormatterSeekBar() {
    @Override
    public String format(float value) {
        return String.format("Rp. %d", (int) value);
    }
});
rangeSeekBar.setListener(new ListenerSeekBar() {
    @Override
    public void valueChanged(RangeSeekBar rangeSeekBar, float currentValue) {
        Log.d("_TAG", "valueChanged: "+currentValue);
    }
});
```

- Example 2

<p align="center">
  <img src="https://github.com/gzeinnumer/EasyRangeSeekBar/blob/remove-step-v2/preview/example6.jpg" width="400"/>
</p>

```xml
<com.gzeinnumer.ers.RangeSeekBar
    android:id="@+id/rsb"
    android:layout_width="250dp"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    app:rsb_leftColor="@color/colorLeft"
    app:rsb_rightColor="@color/colorRight"
    app:rsb_showBubble="false"
    app:rsb_smallDot="true"
    app:rsb_textFollowDot="true"
    app:rsb_textFollowRegionColor="true"/>
```
```java
RangeSeekBar rangeSeekBar = (RangeSeekBar) findViewById(R.id.rsb);
rangeSeekBar.setMax(3000);
rangeSeekBar.setCurrentValue(1500);
rangeSeekBar.setRegionTextFormatter(new RangeSeekBar.RegionTextFormatter() {
    @Override
    public String format(int region, float value) {
        return String.format("region %d : %d", region, (int) value);
    }
});
```

---
# Example Code/App

[Sample Code And App](https://github.com/gzeinnumer/EasyRangeSeekBarExample)

---
# Version
- **1.0.0**
  - First Release

---
# Contribution
You can sent your constibution to `branch` `open-pull`.

### Fore More [All My Library](https://github.com/gzeinnumer#my-library-list)

---

```
Copyright 2021 M. Fadli Zein
```
