import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'image_picker_config_model.dart';
import 'image_picker_platform_interface.dart';

/// An implementation of [ImagePickerPlatform] that uses method channels.
class MethodChannelImagePicker extends ImagePickerPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('image_picker');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<Map> openGallery(ImagePickerConfigModel configModel) async {
    return await methodChannel.invokeMethod<Map>('openGallery', configModel.toMap()).then((value) => value??{"code": 2, "msg": "OpenGallery Error"});
  }
}
