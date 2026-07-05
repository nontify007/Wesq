# Totem Auto Equip (Fabric Mod สำหรับ Minecraft 26.1.2)

ม็อดนี้จะทำงานเมื่อคุณ**เปิดกระเป๋า (กด E)**:
- ถ้ามือซ้าย (offhand) **ไม่มี** Totem of Undying → ระบบจะหา Totem ในกระเป๋า/hotbar แล้วเอาไปใส่ offhand ให้อัตโนมัติ
- ถ้ามือซ้ายมีของอย่างอื่นอยู่ → ของชิ้นนั้นจะถูกสลับออก และ Totem จะเข้าไปแทนที่
- ถ้ามือซ้ายมี Totem อยู่แล้ว → ไม่ทำอะไร

**เปิด/ปิดการทำงาน**: กดปุ่ม `Right Shift` (แก้ปุ่มได้ที่ Options → Controls → หมวด "Totem Auto Equip")

⚠️ เขียนเฉพาะสำหรับ **Minecraft 26.1.2** ขึ้นไป (เวอร์ชันที่เปลี่ยนมาใช้ Mojang official mappings ไม่ obfuscate โค้ดแล้ว) ใช้กับ Minecraft เวอร์ชันเก่ากว่า 26.1 ไม่ได้

---

## ⚠️ ทำไมผมส่งไฟล์ source แทน .jar ที่คอมไพล์แล้ว

การคอมไพล์ Fabric mod ต้องใช้ Gradle ดาวน์โหลดไฟล์จาก maven.fabricmc.net และ Mojang libraries
ซึ่งสภาพแวดล้อมที่ผมรันโค้ดอยู่เข้าถึงไม่ได้ ผมจึงคอมไพล์ .jar ให้ในนี้ไม่ได้โดยตรง

แต่โปรเจกต์นี้**สมบูรณ์พร้อม build** แล้ว และมี GitHub Actions ให้ build อัตโนมัติ (แนะนำ)

## วิธี Build เป็น .jar

### ตัวเลือก 0: ใช้ GitHub Actions (ไม่ต้องติดตั้งอะไรในเครื่องเลย) ⭐ แนะนำ
โปรเจกต์นี้มีไฟล์ `.github/workflows/build.yml` ที่สั่งให้ GitHub คอมไพล์ jar ให้อัตโนมัติ

1. สมัคร GitHub ฟรีที่ https://github.com (ถ้ายังไม่มี)
2. สร้าง repository ใหม่
3. อัปโหลดไฟล์ทั้งหมดในโฟลเดอร์นี้ขึ้น repo (รวมถึงโฟลเดอร์ `.github` และ `gradle` ที่ขึ้นต้นด้วยจุด/ซ่อนอยู่ด้วย!)
4. เข้าไปที่แท็บ **Actions** รอ build เสร็จ (วงกลมเขียวติ๊กถูก)
5. เลื่อนลงล่างสุดของหน้า build นั้น กด **Artifacts** → โหลด `totemautoequip-jar`
6. แตก zip ได้ไฟล์ `.jar` พร้อมใช้

### ตัวเลือก A: ใช้ IntelliJ IDEA (ถ้าอยากแก้โค้ดเองด้วย)
1. ติดตั้ง **JDK 25** (จำเป็น! Minecraft 26.1 ต้องใช้ Java 25 ขั้นต่ำ) — https://adoptium.net/
2. ติดตั้ง **IntelliJ IDEA 2025.3 ขึ้นไป** (เวอร์ชันเก่ากว่านี้ mixin จะมีปัญหา)
3. เปิดโฟลเดอร์นี้ทั้งโฟลเดอร์ด้วย IntelliJ
4. รอ Gradle sync (จะโหลด Minecraft/Fabric libraries เอง)
5. เปิดแท็บ Gradle ด้านขวา → `Tasks > build > build`
6. ได้ไฟล์ jar ที่ `build/libs/totemautoequip-1.0.0.jar`

### ตัวเลือก B: ใช้ command line
```bash
cd totemautoequip
./gradlew build
```
ได้ jar ที่ `build/libs/totemautoequip-1.0.0.jar`

## หลัง build เสร็จ
เอาไฟล์ `totemautoequip-1.0.0.jar` (**ไม่ใช่** ตัวที่มีคำว่า `-sources` ต่อท้าย) ไปวางในโฟลเดอร์ `mods` ของ Fabric
ต้องมี **Fabric Loader** (>=0.19.3) และ **Fabric API** (เวอร์ชันตรงกับ 26.1.2) ติดตั้งไว้ด้วย

## โครงสร้างไฟล์
```
totemautoequip/
├── build.gradle
├── settings.gradle
├── gradle.properties
├── gradlew / gradlew.bat / gradle/wrapper/  (Gradle Wrapper)
├── LICENSE
└── src/main/
    ├── java/com/totemswap/totemautoequip/TotemAutoEquipClient.java   <- โค้ดหลัก
    └── resources/
        ├── fabric.mod.json
        └── assets/totemautoequip/lang/ (en_us.json, th_th.json)
```
