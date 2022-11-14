import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import { multiply , openCamera, openGallery,editImage} from 'mazadat-image-picker';

export default function App() {
  const [result, setResult] = React.useState<String | undefined>();

  React.useEffect(() => {
    const path="data/user/0/com.mazadatimagepickerexample/cache/1668422775873.png"
    editImage(path,"Scan Front Side",4,3).then(setResult);
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
