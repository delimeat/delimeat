const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {
	mode : 'production',
	devtool : 'source-map',
	entry : {
		'polyfills': './src/polyfills.ts',
		'vendors' : './src/vendors.ts',
		'app' : './src/main.ts'
	},
	output : {
		path : path.resolve(__dirname, 'target/classes/META-INF/resources'),
		filename : 'js/[name]-[hash].js'
	},
	resolve : {
		extensions : [ '.ts', '.js' ]
	},
	plugins : [
		new HtmlWebpackPlugin({
			template : './src/index.html',
			favicon : './src/favicon.ico',
			minify : {
				collapseWhitespace : true,
				removeComments : true,
				removeRedundantAttributes : true,
				removeScriptTypeAttributes : true,
				removeStyleLinkTypeAttributes : true
			}
		}),
		new webpack.ProvidePlugin({
			$ : "jquery",
			jQuery : "jquery"
		}),
		new CopyWebpackPlugin([ {
			from : './src/i18n',
			to : 'i18n'
		} ])
	],
	module : {
		rules : [
			{
				test : /\.ts$/,
				use : [
					'awesome-typescript-loader',
					'angular2-template-loader'
				]
			},
			{
				test : /\.html$/,
				use : [
					'html-loader'
				]
			},
			{
				test : /\.css$/,
				use : [
					"style-loader",
					"css-loader"
				]
			}, 
			{
				test : /\.(png|jpe?g|gif|svg|woff|woff2|ttf|eot|ico)$/,
				use : [
					{
						loader : 'file-loader',
						options : {
							outputPath: 'assets/'
						}
					}
				]
			}

		]
	}
};