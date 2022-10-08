
import 'image_picker_config_model.dart';
import 'image_picker_platform_interface.dart';

class ImagePicker {
  Future<String?> getPlatformVersion() {
    return ImagePickerPlatform.instance.getPlatformVersion();
  }

  Future<Map> openGallery(ImagePickerConfigModel configModel) {
    return ImagePickerPlatform.instance.openGallery(configModel);
  }

}
