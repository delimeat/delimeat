var gulp = require('gulp');
var del = require('del');

var $ = require('gulp-load-plugins')({
  rename: {
    'gulp-ng-annotate': 'annotate'
  }
});
 
// Clean output directory
gulp.task('clean', function(){
	return del(['.tmp', 'build/*'], {dot: true});	
});

gulp.task('compile',['clean'], function() {
	return gulp.src('./src/main/sourceapp/*.html')
    .pipe($.usemin({
        css:          [$.cssnano(),$.rev()],
        html:         [$.htmlmin({collapseWhitespace: true})],
        js:           [$.annotate(), $.uglify(), $.rev()],
        js_libs:      [$.annotate(), $.uglify(), $.rev()]
    }))
    .pipe($.size({title: 'html',showFiles:true}))
	.pipe(gulp.dest('build'));
});


gulp.task('copy',['compile'], function() {
    return gulp.src('build/**/*')
    .pipe(gulp.dest('src/main/webapp'));
});

gulp.task('default',['clean','compile','copy'], function() {
  // place code for your default task here
});