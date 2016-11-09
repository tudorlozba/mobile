import React, { Component } from 'react';
import { View, Text, StyleSheet, TouchableHighlight, TextInput, ListView } from 'react-native';


export default class CodeSharing extends Component {

  constructor(props) {
    super(props);

    const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});

    this.state = { name : "CodeSharing",
                   dataSource: ds.cloneWithRows([
                   'John', 'Joel', 'James', 'Jimmy', 'Jackson', 'Jillian', 'Julie', 'Devin'
      ])};
  }


  _create = (data) => {
    console.log("create");
    fetch('https://mywebsite.com/mydata.json')
  };

  _retreave = (data) => {
    console.log("retreave");
    fetch('https://mywebsite.com/mydata.json')
  };

  _update = (data) => {
    console.log("update");
    fetch('https://mywebsite.com/mydata.json')
  };

  _delete = (data) => {
    console.log("delete");
    fetch('https://mywebsite.com/mydata.json')
  };

  render() {
    return (
      <View style={styles.container}>
        <Text>Hello this is </Text>
        <Text style={styles.text_title}>{this.state.name}</Text>

        <CustomTextInput text="Create" placeholder="Create element" click={this._create}/>
        <CustomTextInput text="Retreive" placeholder="Retrieve element" click={this._retreave}/>
        <CustomTextInput text="Update" placeholder="Update element" click={this._update}/>
        <CustomTextInput text="Delete" placeholder="Delete element" click={this._delete}/>
        <ListView
              dataSource={this.state.dataSource}
              renderRow={(rowData) => <View><Text>{rowData}</Text><Text>aaaaa</Text></View>}
        />
      </View>


    )
  }
}

class CustomTextInput extends Component {
  // Initial state
  state = {
      command : ""
  };



  render() {
    return (
      <View style={{padding: 10}}>
        <TextInput
          style={{height: 40}, {width:300}}
          placeholder={this.props.placeholder}
          onChangeText={(text) => { this.setState({command:text}) } }

        />
        <TouchableHighlight onPress={this.props.click}>
          <Text style={{height: 40}, {paddingTop: 10}, {paddingLeft:5}}>{this.props.text}</Text>
        </TouchableHighlight>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },

  text_title: {
      color: 'red',
      fontSize: 35
  }
});
