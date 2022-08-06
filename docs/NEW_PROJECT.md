# Configure a new project from the scratch

### 1 - Go to Gitlab...

... and create blank project within the corresponding subgroup, and clone it to your local workspace.
Click the "Initialize repository with a README" checkbox.
> NOTE: If you don't have permission to do so, ask a tech leader

### 2 - Download this project in zip format...

... and extract it into the folder where you cloned the new project.
After that, open the project  in Android Studio.

> NOTE: Also copy or create the `.gitignore` file.

### 3 - Change the name

Go to `settings.gradle` and modify the value on `rootProject.name` to your project name.
```
rootProject.name=TestApp
```

### 4 - Change the base service url and database file name

Go to `gradle.properties` and change the values for the following:        

- `base_dev_url` with the development backend url. Ex: `https://www.dev.exampl.com/api/v1/`
- `base_stg_url` with the staging/qa backend url. Ex: `https://www.stg.exampl.com/api/v1/`
- `base_prod_url` with the production backend url. Ex: `https://www.exampl.com/api/v1/`

- For `db_dev_name`, `db_stg_name` and `db_prod_name` change [APP_NAME] with your project name.     
Ex:
```
db_dev_name="test_app_db_dev"
db_stg_name="test_app_db_stg"
db_prod_name="test_app_db_prod"
```

### 5 - Change the packagename

Initially the package name is `com.thinkup.mvi`. You will change it to match with your project.     
Ex: `com.test.app`.

To do it, go to the app folder and position yourself on the `project` folder. Rename it for project name (Ex: app).         
If you want to modify the company part, position yourself on the `thinkup` folder and make the same process.           

Check your manifest and app build.gradle to make sure the packagename match with your new physically name.          

### 6 - Create Firebase account

Go to the [Firebase console](https://console.firebase.google.com/u/0/) and create a new project:        
1 - Enter the project name and complete the creation. Ex: TestApp           
2 - Add a new android app.          
- Enter the packagename. Same as the step 5.            
- Enter your project name. Same as the step 3.          
- Register the app. 
        
3 - Download the json file and copy it into the `app` folder.           
4 - Skip the step 3. The SDK is already implemented in the project.         
5 - And that it's. You add the production flavor so far. Repeat the process for develop and staging packages.           
Ex:  `com.test.app.develop` and `com.test.app.staging`.         


You can run your app now! but you still need an extra settings.         

### 7 - Enable Crashlytics

In the Firebase console (using the project created previously).         
For each environment you might enable Crashlytics feature (side menu> Crashlytics) and force a crash to start
to receive the crashes stacktrace.

### 8 - Enable Firebase App Distribution

In the Firebase console (using the project created previously).
For each environment you might enable Firebase App Distribution feature (side menu> App Distribution) and click "Get Started"
to be able to upload from the terminal.

### 9 - Setup Fastlane

Check the fastlane installation running this command:           
```
ruby --version
```
Check the fastlane installation running this command:           
```
fastlane -- version
```
If you haven't installed ruby or fastlane, install it following the documentation [here](https://docs.fastlane.tools/getting-started/android/setup/)         
Install:            
    - `gem install bundler`         
    - `bundle update`           
    - `brew install fastlane`           
Also needs install firebase plugin from [here](https://firebase.google.com/docs/app-distribution/android/distribute-fastlane?apptype=aab):          
    - `fastlane add_plugin firebase_app_distribution`           
and versioning plugin:          
    - `fastlane add_plugin versioning_android`

### 10 - Changelog

For each PR or commit into develop branch, don't forget to write a line in the changelog file (changelog/current.txt).
Including details of your task, a TeamWork link or samething related to your code.

> **IMPORTANT**: Before release a version you must cut the content of the current.txt and paste it into a new file named same as your next version. Ex: If you want release a staging version with the name 1.0.0, this version will be named like 1.0.0-alpha. So, your changelog file would be named as 1.0.0-alpha.txt in the same path as current.txt.

> NOTE: If you miss this step, the release from the terminal will fail.

> NOTE: After release it your current.txt must be empty to avoid include the same details in future versions.

### 11 - Setting fastlane environment variables

Make a copy of the `.env.example` file into the fastlane folder and rename it to `.env`.           

```
APP_NAME="[app name]" --> Put your app name. Ex: TestApp
APP_FLAVOR="[develop | staging | production]" --> Choose your default environment. Ex: staging
FIREBASE_CLI_TOKEN="[firebase token to upload]" --> Check the code below to generate it.
SLACK_URL="[slack webhook to post success message]" --> Use always the same.
```

Finally, sign in to your Google Account via the plugin's login action. Check how do it [here](https://firebase.google.com/docs/app-distribution/android/distribute-fastlane?authuser=0&apptype=apk#google-acc-fastlane).            
    - `bundle exec fastlane run firebase_app_distribution_login`            

> NOTE: Make sure not commit the `.env` file and not share your `FIREBASE_CLI_TOKEN`.

If everything it's fine, you can run a fastlane command to upload a new version:                
```
fastlane release_app
```
Also you can add custom params like:            
- type --> APK or AAB           
- flavor --> dev | staging | production         
- app_id --> 1:105449870[...]3cd42f92b182df7ebeaf           
With this you can release to a different environment than the default.           

Example:            
```
fastlane release_app type:AAB flavor:dev app_id:1:105449870[...]3cd42f92b182df7ebeaf
```

### EXTRA

You probably want to check all the content before push to your client. See here a list of file to review:

- README.md ==> you could modify it
- docs/ (all content of the folder)
- Check all the TODO's in the code

