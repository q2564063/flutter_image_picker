import 'package:flutter_test/flutter_test.dart';
import 'package:image_picker/image_picker.dart';
import 'package:image_picker/image_picker_platform_interface.dart';
import 'package:image_picker/image_picker_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockImagePickerPlatform 
    with MockPlatformInterfaceMixin
    implements ImagePickerPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final ImagePickerPlatform initialPlatform = ImagePickerPlatform.instance;

  test('$MethodChannelImagePicker is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelImagePicker>());
  });

  test('getPlatformVersion', () async {
    ImagePicker imagePickerPlugin = ImagePicker();
    MockImagePickerPlatform fakePlatform = MockImagePickerPlatform();
    ImagePickerPlatform.instance = fakePlatform;
  
    expect(await imagePickerPlugin.getPlatformVersion(), '42');
  });
}
