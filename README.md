# CODELAB Android

O objetivo desse codelab é fazer um aplicativo que tira uma foto usando a câmera do device/emulador
e postar em uma rede social, usando apenas os recursos do sdk do Android.

### Download do projeto

1- Baixe o código

```
git clone https://github.com/anapsil/workshop_unibh.git
```

2- Importe o projeto para o Android Studio

```
Import project (Gradle, Eclipse ADT, etc.)
```

### O AndroidManifest.xml

Todo aplicativo tem que ter um arquivo `AndroidManifest.xml` (precisamente com esse nome) 
no diretório raiz. 

O arquivo de manifesto apresenta informações essenciais 
sobre o aplicativo ao sistema Android, necessárias para o sistema antes que ele 
possa executar o código do aplicativo.

O arquivo de manifesto é usado para: 

* Nomear pacote do aplicativo. O pacote é usado como identificador único do aplicativo.
* Descrever os componentes do aplicativo
* Declarar as permissões que o aplicativo deve ter para acessar partes protegidas da API 
e interagir com outros aplicativos. Ele também declara as permissões que outros devem ter 
para interagir com os componentes do aplicativo.

```
    <manifest xmlns:android="http://schemas.android.com/apk/res/android"
        package="com.example.apsilva.workshopunibh">
    
        <application
            android:allowBackup="true"
            android:icon="@mipmap/ic_launcher"
            android:label="@string/app_name"
            android:roundIcon="@mipmap/ic_launcher_round"
            android:supportsRtl="true"
            android:theme="@style/AppTheme" />
    </manifest>
```

Adicione as seguintes permissões no arquivo `AndroidManifest.xml` do projeto:

```
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.CAMERA" />
```

### Verificando as permissões

Se o aparelho roda com Android 6.0 (API 23) ou superior e `targetSdkVersion` do aplicativo é igual 
ou superior a 23, o usuário não é notificado de nenhuma permissão no momento
da instalação. O aplicativo deve questionar o usuário sobre as [permissões perigosas](https://developer.android.com/guide/topics/permissions/overview?hl=pt-br#permission-groups)
em tempo de execução.

No projeto, abra a classe `MainActivity.kt` e adicione os trechos de código abaixo:

```
    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), PERMISSION_CODE)
        } else {
            takePicture()
        }
    }
    
    ====================================================================================================
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                                grantResults: IntArray) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == PERMISSION_CODE) {
                if (grantResults[0] == PERMISSION_GRANTED) {
                    takePicture()
                } else {
                    showPermissionDeniedMessage()
                }
            }
        }

```

### Configurando do File Provider


```
   <provider
        android:name="FileProviderCompat"
        android:authorities="com.example.apsilva.workshopunibh.fileprovider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/fileprovider" />
   </provider>

```
No diretório de recursos, crie a pasta `xml` e o arquivo `fileprovider.xml`.

Adicione no arquivo criado, o conteúdo abaixo:

```
    <?xml version="1.0" encoding="utf-8"?>
    <paths>
        <external-files-path name="images" path="Pictures" />
    </paths>

```

### Tirando uma foto

Volte para a classe `MainActivity.kt` e inclua os trechos abaixo:
```
    private fun takePicture() {
        imagePath = getPhotoFileUri()
        imageUri = FileProvider.getUriForFile(this,
                "com.example.apsilva.workshopunibh.fileprovider", imagePath!!)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, CAMERA_REQUEST)
    }
    
    ====================================================================================
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            val takenImage = BitmapFactory.decodeFile(imagePath?.absolutePath)
            picture.setImageBitmap(takenImage)
        }
    }

```

### Compartilhando a foto


```
    private fun sharePicture() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://$imagePath"))
        } else {
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_photo)))
    }

```

