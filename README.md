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

Todo aplicativo tem que ter um arquivo AndroidManifest.xml (precisamente com esse nome) 
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

Adicione as seguintes permissões no arquivo AndroidManifest.xml do projeto:

```
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

### Verificando as permissões



### Configurando do File Provider



### Tirando uma foto



### Compartilhando a foto