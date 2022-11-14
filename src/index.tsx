import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'mazadat-image-picker' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const MazadatImagePicker = NativeModules.MazadatImagePicker
  ? NativeModules.MazadatImagePicker
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: number, b: number): Promise<number> {
  return MazadatImagePicker.multiply(a, b);
}

export function openCamera(title :string, aspect_ratio_x: number, aspect_ratio_y: number): Promise<String> {
  return MazadatImagePicker.openCamera(title, aspect_ratio_x,aspect_ratio_y);
}

export function openGallery(title :string, aspect_ratio_x: number, aspect_ratio_y: number): Promise<String> {
  return MazadatImagePicker.openGallery(title, aspect_ratio_x,aspect_ratio_y);
}

export function editImage(path :string,title :string, aspect_ratio_x: number, aspect_ratio_y: number): Promise<String> {
  return MazadatImagePicker.editImage(path,title, aspect_ratio_x,aspect_ratio_y);
}
