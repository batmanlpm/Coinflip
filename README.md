# Coinflip
Coin flip application for Android.

## Features

- **Coin Flip Animation**: Realistic coin flipping animation with rotation, bounce, and scale effects
- **Sound Effects**: Metallic coin flip sound when flipping
- **NSFW Mode**: Toggle switch to enable NSFW mode with pre-loaded custom images
- **Random Image Selection**: In NSFW mode, randomly selects from 5 heads images and 5 tails images
- **Persistent Settings**: Your mode preference is saved between app sessions

## How to Use

1. **Normal Mode**: Simply tap the "Flip Coin" button to flip the coin
2. **NSFW Mode**: 
   - Toggle the "NSFW Mode" switch to enable
   - Flip the coin to see random NSFW images for heads or tails

## Customizing NSFW Images

To add your own NSFW images, replace the placeholder files in:
- `app/src/main/res/drawable/nsfw_heads_1.xml` through `nsfw_heads_5.xml` for heads images
- `app/src/main/res/drawable/nsfw_tails_1.xml` through `nsfw_tails_5.xml` for tails images

You can replace these XML vector files with PNG/JPG images by:
1. Deleting the XML files
2. Adding your images as `nsfw_heads_1.png`, `nsfw_heads_2.png`, etc. in the `drawable` folder

## Building

```bash
./gradlew assembleDebug
```

## Requirements

- Android SDK 24+ (Android 7.0 Nougat or higher)
- Kotlin 1.9.0+

