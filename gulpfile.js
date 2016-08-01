var gulp = require('gulp')
	, del = require('del')
	, lrserver = require('tiny-lr')()
    , express = require('express')
    , $ = require('gulp-load-plugins')({
    	  rename: {
    	    'gulp-ng-annotate': 'ngannotate',
    	    'gulp-angular-templatecache':'ngtemplatecache',
    	    'gulp-ng-config':'ngconfig'
    	  }
    	});

var SERVER_PORT = 5000;
 
// Clean output directory
gulp.task('clean', function(){
	return del(['target/tmp', 'target/build'], {dot: true});	
});

// linting
gulp.task('lint', function() {
    return gulp.src('./src/main/sourceapp/js/**/*.js')
        .pipe($.jshint())
        .pipe($.jshint.reporter('jshint-stylish'))
        .pipe($.jshint.reporter('fail'));
});

gulp.task('views',['clean'], function() {
    return gulp.src('./src/main/sourceapp/js/**/*.tmpl.html')
        .pipe($.ngtemplatecache({
            module: 'app',
            root: 'js'
        }))
        .pipe(gulp.dest('target/tmp/js'));
});

gulp.task('i18n',['clean'],  function () {
	  gulp.src('./src/main/sourceapp/i18n/*.json')
	  .pipe(gulp.dest('target/build/i18n'));
});

gulp.task('images',['clean'], function() {
    return gulp.src('./src/main/sourceapp/img/**/*.*')
        .pipe($.imagemin())
        .pipe(gulp.dest('target/build/img'));
});

gulp.task('glyph-icons',['clean'], function () {
	  gulp.src('./src/main/sourceapp/components/bootstrap/fonts/*')
	  .pipe(gulp.dest('target/build/fonts'));
});

gulp.task('favicon',['clean'], function () {
	  gulp.src('./src/main/sourceapp/*.ico')
	  .pipe(gulp.dest('target/build'));
});

gulp.task('config:dev',['clean'], function () {
	  gulp.src('./src/main/sourceapp/config.json')
	  .pipe($.ngconfig('delimeat.config', {
		  environment: 'development'
		}))
	    .pipe($.rename('config.js'))
	    .pipe(gulp.dest('.tmp/js'));
});

gulp.task('config:build',['clean'], function () {
	  gulp.src('./src/main/sourceapp/config.json')
	  .pipe($.ngconfig('delimeat.config', {
		  environment: 'production'
		}))
	    .pipe($.rename('config.js'))
	    .pipe(gulp.dest('target/tmp/js'));
});

gulp.task('compile',['clean','views','config:build','lint','i18n','images','glyph-icons','favicon'], function() {
	return gulp.src('./src/main/sourceapp/index.html')
    .pipe($.inject(gulp.src('./target/tmp/js/templates.js', {read: false}),
            {
                starttag: '<!-- inject:templates:js -->',
                ignorePath: '/target/tmp'
            }
        ))
    .pipe($.usemin({
        css:          [$.cssnano(),$.rev()],
        html:         [$.htmlmin({collapseWhitespace: true})],
        js:           [$.ngannotate(), $.uglify(), $.rev()],
        js_libs:      [$.ngannotate(), $.uglify(), $.rev()]
    }))
    .pipe($.size({title: 'html',showFiles:true}))
	.pipe(gulp.dest('target/build'));
});

//Serve tasks
gulp.task('reload:html', function () {
    return gulp.src('./src/main/sourceapp/**/*.html')
        .pipe($.livereload(lrserver));
});

gulp.task('watch',['clean'], function () {
    gulp.watch('./src/main/sourceapp/**/*.html', ['reload:html']);
});

gulp.task('serve:dev', ['clean','watch','config:dev'], function() {
    var server = express();
    server.use(express.static('.tmp/'));
    server.use(express.static('./src/main/sourceapp/'));
    server.listen(SERVER_PORT);
});

gulp.task('default',['clean','compile'], function() {
  // place code for your default task here
});