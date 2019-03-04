/* 
eslint-disable
jsx-a11y/click-events-have-key-events,
jsx-a11y/no-static-element-interactions,
jsx-a11y/click-events-have-key-events,
react/forbid-prop-types
*/
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { PacmanLoader } from 'react-spinners';
import { Link } from 'react-router-dom';
import classNames from 'classnames';
import queryString from 'query-string';

import styles from './Compressor.css';
import routes from '../constants/routes';

const { dialog, shell } = require('electron').remote;
const { exec } = require('child_process');
const path = require('path');

export default class Home extends Component {
	static propTypes = {
		location: PropTypes.object.isRequired,
	}

    constructor(props) {
		super(props);

		this.state = {
		processing: false,
		dragging: false,
		inputPath: '',
		outputPath: '',
		error: '',
		};
	}

	componentDidMount() {
		const { location } = this.props;
		const { mode } = queryString.parse(location.search);

		document.addEventListener('drop', (e) => {
			e.preventDefault();
			e.stopPropagation();

			const files = [...e.dataTransfer.files].filter(f => f.path.endsWith(
				mode === 'compress' ? '.txt' : '.u21'
			));

			if (files.length) {
				this.setState({
					processing: true,
					inputPath: path.normalize(e.dataTransfer.files[0].path).replace(/ /g, '\\ '),
				}, () => this.startProcessing());
			} else {
				this.setState({
					error: 'Seleccioná un archivo con la extensión correcta',
				})
			}
		});
		document.addEventListener('dragover', e => {
			e.preventDefault();
			e.stopPropagation();
		});
	}

	componentWillUnmount() {
		//   document.removeEventListener('drop');
		//   document.removeEventListener('dragover');
	}

	startProcessing = () => {
		const { inputPath } = this.state;
		const { location } = this.props;
		const { mode } = queryString.parse(location.search);

		const inputPathArray = inputPath.split(path.sep);
		const newFileName = `${inputPathArray[inputPathArray.length - 1].split('.')[0]}.${mode === 'compress' ? 'u21' : 'txt'}`;
		const outputPath = path.normalize(`${inputPathArray.slice(0, inputPathArray.length - 1).join(path.sep)}/${newFileName}`).replace(/ /g, '\\ ');

		const filePath = process.env.NODE_ENV === 'development' ? path.normalize('app/jar/TAED2-FinalProject.jar') : path.normalize(`${process.resourcesPath}/../app/jar/TAED2-FinalProject.jar`).replace(/ /g, '\\ ');
		const command = `java -jar ${filePath} ${mode === 'compress' ? '-c' : '-d'} ${inputPath} ${outputPath}`;

		exec(
			command,
			(err, stdout, stderr) => {
				console.log(stdout, stderr)
				if (err) {
					console.error(err)
					this.setState({
						error: 'Hubo un error durante el procesamiento del archivo.'
					});
				}
				this.setState({
					outputPath,
				})
			}
		);
	}

	enableDragging = () => {
		const { dragging } = this.state;
		if (!dragging) {
		this.setState({
			dragging: true,
		})
		}
	}

	disableDragging = () => {
		this.setState({
			dragging: false,
		})
	}

	onDrop = () => {
		this.setState({
			dragging: false,
		});
	}

	openOutputDir = () => {
		const { outputPath } = this.state;

		const outputPathSplitted = outputPath.split(path.sep);
		const outputDir = outputPathSplitted.slice(0, outputPathSplitted.length - 1).join(path.sep);
		shell.openItem(outputDir);
	}

	showFilePicker = () => {
		const { location } = this.props;
		const { mode } = queryString.parse(location.search);

		const filters = mode === 'compress' 
			? [{ name: 'Text', extensions: ['txt'] }]
			: [{ name: 'U21', extensions: ['u21'] }]

		dialog.showOpenDialog({
			properties: ['openFile'],
			filters
		}, (filePaths) => {
			this.setState({
				processing: true,
				inputPath: path.normalize(filePaths[0]).replace(/ /g, '\\ '),
			}, () => this.startProcessing());
		})
	}

	renderDragNDrop = () => {
		const { processing, dragging, error } = this.state;
		const { location } = this.props;
		const { mode } = queryString.parse(location.search);

		return processing ? (
			<div className={styles.spinnerWrapper}>
				<div style={{ alignSelf: 'flex-start', marginLeft: 20 }}>
					<PacmanLoader
						sizeUnit="px"
						size={30}
						color="#ccc02e"
						loading
					/>
				</div>
				<br /><br />
				<p>Procesando archivo...</p>
			</div>
		) : (
			<div
				onClick={this.showFilePicker}
				onDragOver={() => this.enableDragging()}
				onDragLeave={() => this.disableDragging()}
				onDrop={() => this.disableDragging()}
				className={classNames(styles.dropFiles, { [styles.dropFilesActive]: dragging })}
			>
				<i className={`${dragging ? 'far' : 'fas'} fa-file fa-3x ${styles.fileIcon}`} />
				<p className={styles.dropFilesText}>
					{ dragging ? 'Soltá el archivo acá' : 'Arrastrá un archivo o hacé click para seleccionarlo.' }
					{ !dragging && mode === 'compress' && ' Extensiones permitidas: txt' }
					{ !dragging && mode === 'uncompress' && ' El archivo debe ser de extensión .u21' }
				</p>
				{ error && (
					<p className={classNames(styles.dropFilesText, styles.errorText)}>
						{error}
					</p>
				)}
			</div>
		)
	}

	render() {
		const { outputPath } = this.state;
		const { location } = this.props;
		const { mode } = queryString.parse(location.search);

		return (
			<div className={styles.container} data-tid="container">
				<div className={styles.backButton} data-tid="backButton">
					<Link to={routes.HOME}>
						<i className="fa fa-arrow-left fa-3x" />
					</Link>
				</div>
				{ outputPath
				? (
					<div className={styles.resultsWrapper} onClick={this.openOutputDir}>
						<h2 className={styles.resultsTitle}>¡Archivo {mode === 'compress' ? 'comprimido' : 'descomprimido'} con éxito!</h2>
						<i className="fas fa-folder-open fa-4x" />
						<p className={styles.options}>Ver en explorador de archivos</p>
					</div>
				)
				: this.renderDragNDrop()}
			</div>
		);
  }
}
