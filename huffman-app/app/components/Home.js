/* eslint-disable compat/compat */
import React, { Component } from 'react';

import styles from './Home.css';

const { exec } = require('child_process');

export default class Home extends Component {
  componentDidMount() {
    this.isJavaInstalled()
      .then(Function.prototype)
      .catch(() => alert('Necesitás instalar Java para correr este programa'));
  }

  goToPage = (mode) => {
    this.props.history.push(`/compressor?mode=${mode}`);
  }

  isJavaInstalled = () => new Promise((resolve, reject) => {
    exec('java -version', (error) => {
      if (error) {
        console.log(error);
        reject();
      }
      resolve();
    });
  });

  render() {
    return (
      <div className={styles.container} data-tid="container">
        <h2>Compresor Huffman</h2>
        <h4>Seleccioná la operación que querés realizar</h4>
        <div className={styles.optionsWrapper}>
          <div onClick={() => this.goToPage('compress')} className={styles.optionWrapper}>
            <i className="far fa-file-archive fa-3x" />
            <p className={styles.options}>Comprimir un archivo</p>
          </div>
          <div onClick={() => this.goToPage('uncompress')} className={styles.optionWrapper}>
            <i className="fas fa-file-export fa-3x" />
            <p className={styles.options}>Descomprimir un archivo .u21</p>
          </div>
        </div>
      </div>
    );
  }
}
