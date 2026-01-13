# Android Views

A collection of beautiful, customizable Android UI components for both traditional Views and Jetpack Compose.

![Particles View](https://onexeor.dev/images/github/dev.onexeor.views.particles-view_1.gif)
![Pulse Button](https://onexeor.dev/images/github/dev.onexeor.views.pulse-button_2.gif)
![Particle Progress Bar](https://onexeor.dev/images/github/dev.onexeor.views.particles-progress-bar_1.gif)
![Bottom Navigation Bar](https://onexeor.dev/images/github/dev.onexeor.views.bottom-navigation-bar_1.png)

## Components

| Component | Description | View | Compose |
|-----------|-------------|:----:|:-------:|
| **ParticlesView** | Animated particle network background with touch interaction | Yes | - |
| **ParticleProgressBar** | Circular loading indicator with orbiting particles | Yes | Yes |
| **PulseButton** | Animated button with expanding pulse waves | Yes | - |
| **BottomNavigationBar** | Bottom navigation with center button cutout | Yes | - |

## Installation

Add JitPack repository to your root `build.gradle`:

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Add dependency for the component you need:

```gradle
dependencies {
    // Traditional Views
    implementation 'com.github.OneXeor.Android-Views:particles_view:1.0.0'
    implementation 'com.github.OneXeor.Android-Views:particle_progress_bar:1.0.0'
    implementation 'com.github.OneXeor.Android-Views:pulse_button:1.0.0'
    implementation 'com.github.OneXeor.Android-Views:bottomnavigationbar:1.0.0'

    // Jetpack Compose
    implementation 'com.github.OneXeor.Android-Views:compose-views:1.0.0'
}
```

## Usage

### ParticlesView

An animated particle network background with interactive touch support.

```xml
<com.onexeor.particlesview.ParticlesView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:pv_nodes_count="30"
    app:pv_nodes_size="4dp"
    app:pv_nodes_color="#FFFFFF"
    app:pv_nodes_speed_min="1"
    app:pv_nodes_speed_max="3"
    app:pv_background_color="#1A1A2E"
    app:pv_linking_nodes_distance="200dp"
    app:pv_linking_line_color="#80FFFFFF"
    app:pv_linking_line_width="1dp"
    app:pv_touchable="true"
    app:pv_touchable_radius="100dp" />
```

### ParticleProgressBar

A circular loading indicator with orbiting particles.

**XML View:**
```xml
<io.singulart.particle_progress_bar.ParticleProgressBar
    android:layout_width="100dp"
    android:layout_height="100dp"
    app:ppb_enabled="true"
    app:ppb_color="#3399FF"
    app:ppb_count_balls="8" />
```

**Jetpack Compose:**
```kotlin
CircularDotProgress(
    dotCount = 8,
    dotColor = Color(0xFF3399FF),
    radius = 80f,
    minDotSize = 10f,
    maxDotSize = 16f
)
```

### PulseButton

An animated button with pulsing wave effects.

```xml
<io.singulart.pulse_button.PulseButton
    android:layout_width="80dp"
    android:layout_height="80dp"
    app:pb_src="@drawable/ic_play"
    app:pb_color_center_circle="#3399FF"
    app:pb_color_center_circle_stroke="#FFFFFF"
    app:pb_stroke_width_of_center_circle="2dp"
    app:pb_stroke_width_of_outer_circle="2dp"
    app:pb_color_wave_circle_stroke="#803399FF"
    app:pb_wave_padding="20dp"
    app:pb_pulse_speed="90" />
```

### BottomNavigationBar

A customizable bottom navigation bar with optional center button cutout.

```xml
<io.singulart.bottomnavigationbar.BottomNavigationBar
    android:layout_width="match_parent"
    android:layout_height="56dp"
    android:layout_gravity="bottom"
    app:bnb_items="@menu/bottom_nav_menu"
    app:bnb_background_color="#FFFFFF"
    app:bnb_item_selected_color="#3399FF"
    app:bnb_text_item_color="#757575"
    app:bnb_text_item_size="10sp"
    app:bnb_center_btn="true"
    app:bnb_cutout_deep="24dp"
    app:bnb_shadow_radius="8dp" />
```

**Menu Resource Example:**
```xml
<!-- res/menu/bottom_nav_menu.xml -->
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@+id/nav_home"
        android:icon="@drawable/ic_home"
        android:title="Home" />
    <item android:id="@+id/nav_search"
        android:icon="@drawable/ic_search"
        android:title="Search" />
    <item android:id="@+id/nav_profile"
        android:icon="@drawable/ic_profile"
        android:title="Profile" />
</menu>
```

**Selection Listener:**
```kotlin
bottomNavigationBar.setOnItemSelectedListener { menuItem ->
    when (menuItem.itemId) {
        R.id.nav_home -> { /* Handle home */ }
        R.id.nav_search -> { /* Handle search */ }
        R.id.nav_profile -> { /* Handle profile */ }
    }
}
```

## Requirements

- **Min SDK:** 21 (Android 5.0 Lollipop)
- **Compile SDK:** 34
- **Jetpack Compose:** 1.5+ (for compose-views module)

## License

```
Copyright 2019 OneXeor

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

## Contributing

Contributions are welcome! Please read our [Contributing Guidelines](CONTRIBUTING.md) and [Code of Conduct](CODE_OF_CONDUCT.md) before submitting pull requests.
