import Main from '@/components/main'
// import parentView from '@/components/parent-view'

/**
 * iview-admin中meta除了原生参数外可配置的参数:
 * meta: {
 *  title: { String|Number|Function }
 *         显示在侧边栏、面包屑和标签栏的文字
 *         使用'{{ 多语言字段 }}'形式结合多语言使用，例子看多语言的路由配置;
 *         可以传入一个回调函数，参数是当前路由对象，例子看动态路由和带参路由
 *  hideInBread: (false) 设为true后此级路由将不会出现在面包屑中
 *  hideInMenu: (false) 设为true后在左侧菜单不会显示该页面选项
 *  notCache: (false) 设为true后页面在切换标签后不会缓存，如果需要缓存，无需设置这个字段，而且需要设置页面组件name属性和路由配置的name一致
 *  access: (null) 可访问该页面的权限数组，当前路由设置的权限会影响子路由
 *  icon: (-) 该页面在左侧菜单、面包屑和标签导航处显示的图标，如果是自定义图标，需要在图标名称前加下划线'_'
 *  beforeCloseName: (-) 设置该字段，则在关闭当前tab页时会去'@/router/before-close.js'里寻找该字段名对应的方法，作为关闭前的钩子函数
 * }
 */

export default [
  {
    path: '/login',
    name: 'login',
    meta: {
      title: 'Login - 登录',
      hideInMenu: true
    },
    component: () => import('@/view/login/login.vue')
  },
  {
    path: '/',
    name: '_home',
    redirect: '/home',
    component: Main,
    meta: {
      hideInMenu: true,
      notCache: true
    },
    children: [
      {
        path: '/home',
        name: 'home',
        meta: {
          hideInMenu: true,
          title: '首页',
          notCache: true,
          icon: 'md-home'
        },
        component: () => import('@/view/single-page/home')
      }
    ]
  },
  {
    path: '/examination',
    name: 'examination',
    meta: {
      icon: 'ios-document-outline',
      title: '考试管理',
      showAlways: true,
      access: ['exam']
    },
    component: Main,
    children: [
      {
        path: '/paper',
        name: 'paper',
        meta: {
          icon: 'ios-book-outline',
          title: '我的试卷',
          access: ['paper:manage']
        },
        component: () => import('@/view/pages/examination/paper/paper.vue')
      },
      {
        path: '/questionBank',
        name: 'questionBank',
        meta: {
          icon: 'ios-document-outline',
          title: '我的题库',
          access: ['bank:manage']
        },
        component: () => import('@/view/pages/examination/questionBank/questionBank.vue')
      },
      {
        path: '/question',
        name: 'question',
        meta: {
          hideInMenu: true,
          access: ['bank:question:manage']
        },
        component: () => import('@/view/pages/examination/questionBank/question.vue')
      },
      {
        path: '/paperEditor',
        name: 'paperEditor',
        meta: {
          hideInMenu: true,
          access: ['paper:set']
        },
        component: () => import('@/view/pages/examination/paper/paperEditor.vue')
      }
    ]
  },
  {
    path: '/curriculum',
    name: 'curriculum',
    meta: {
      icon: 'ios-desktop-outline',
      title: '线上培训',
      showAlways: true,
      access: ['train']
    },
    component: Main,
    children: [
      {
        path: 'class',
        name: 'class',
        meta: {
          icon: 'ios-book-outline',
          title: '课程管理',
          access: ['class:manage']
        },
        component: () => import('@/view/pages/curriculum/class/class.vue')
      },
      {
        path: 'courseware',
        name: 'courseware',
        meta: {
          icon: 'ios-document-outline',
          title: '课件管理',
          access: ['courseware:manage']
        },
        component: () => import('@/view/pages/curriculum/courseware/courseware.vue')
      }
    ]
  },
  {
    path: '/system',
    name: 'system',
    meta: {
      icon: 'ios-settings-outline',
      title: '系统管理',
      showAlways: true,
      access: ['system']
    },
    component: Main,
    children: [
      {
        path: 'user',
        name: 'user',
        meta: {
          icon: 'md-people',
          title: '成员管理',
          access: ['user:manage']
        },
        component: () => import('@/view/pages/system/user/user.vue')
      },
      {
        path: 'role_page',
        name: 'role_page',
        meta: {
          icon: 'md-ribbon',
          title: '角色管理',
          access: ['role:manage']
        },
        component: () => import('@/view/pages/system/role/role.vue')
      },
      {
        path: 'organization',
        name: 'organization',
        meta: {
          icon: 'md-git-network',
          title: '组织架构',
          access: ['organization:manage']
        },
        component: () => import('@/view/pages/system/organization/organization.vue')
      }
    ]
  },
  // {
  //   path: '/example_demo',
  //   name: 'example_demo',
  //   meta: {
  //     icon: 'logo-buffer',
  //     title: '组件示例',
  //     showAlways: true
  //   },
  //   component: Main,
  //   children: [
  //     {
  //       path: 'text_editor',
  //       name: 'text_editor',
  //       meta: {
  //         icon: 'md-funnel',
  //         title: '文本编辑器',
  //         showAlways: true
  //       },
  //       component: parentView,
  //       children: [
  //         {
  //           path: 'demo',
  //           name: 'demo',
  //           meta: {
  //             icon: 'md-flower',
  //             title: 'tinymce编辑器'
  //           },
  //           component: () => import('@/view/example/text-editor/demo.vue')
  //         },
  //         {
  //           path: 'markdown_page',
  //           name: 'markdown_page',
  //           meta: {
  //             icon: 'logo-markdown',
  //             title: 'Markdown编辑器'
  //           },
  //           component: () => import('@/view/example/markdown/markdown.vue')
  //         },
  //         {
  //           path: 'editor_page',
  //           name: 'editor_page',
  //           meta: {
  //             icon: 'ios-create',
  //             title: 'wangeditor编辑器'
  //           },
  //           component: () => import('@/view/example/editor/editor.vue')
  //         }
  //       ]
  //     },
  //     {
  //       path: '/update',
  //       name: 'update',
  //       meta: {
  //         icon: 'md-cloud-upload',
  //         title: '数据上传'
  //       },
  //       component: parentView,
  //       children: [
  //         {
  //           path: 'update_table_page',
  //           name: 'update_table_page',
  //           meta: {
  //             icon: 'ios-document',
  //             title: '上传Csv'
  //           },
  //           component: () => import('@/view/example/update/update-table.vue')
  //         },
  //         {
  //           path: 'update_paste_page',
  //           name: 'update_paste_page',
  //           meta: {
  //             icon: 'md-clipboard',
  //             title: '粘贴表格数据'
  //           },
  //           component: () => import('@/view/example/update/update-paste.vue')
  //         }
  //       ]
  //     },
  //     {
  //       path: '/excel',
  //       name: 'excel',
  //       meta: {
  //         icon: 'ios-stats',
  //         title: 'EXCEL导入导出'
  //       },
  //       component: parentView,
  //       children: [
  //         {
  //           path: 'upload-excel',
  //           name: 'upload-excel',
  //           meta: {
  //             icon: 'md-add',
  //             title: '导入EXCEL'
  //           },
  //           component: () => import('@/view/example/excel/upload-excel.vue')
  //         },
  //         {
  //           path: 'export-excel',
  //           name: 'export-excel',
  //           meta: {
  //             icon: 'md-download',
  //             title: '导出EXCEL'
  //           },
  //           component: () => import('@/view/example/excel/export-excel.vue')
  //         }
  //       ]
  //     },
  //     {
  //       path: 'count_to_page',
  //       name: 'count_to_page',
  //       meta: {
  //         icon: 'md-trending-up',
  //         title: '数字渐变'
  //       },
  //       component: () => import('@/view/example/count-to/count-to.vue')
  //     },
  //     {
  //       path: 'drag_list_page',
  //       name: 'drag_list_page',
  //       meta: {
  //         icon: 'ios-infinite',
  //         title: '拖拽列表'
  //       },
  //       component: () => import('@/view/example/drag-list/drag-list.vue')
  //     },
  //     {
  //       path: 'drag_drawer_page',
  //       name: 'drag_drawer_page',
  //       meta: {
  //         icon: 'md-list',
  //         title: '可拖拽抽屉'
  //       },
  //       component: () => import('@/view/example/drag-drawer')
  //     },
  //     {
  //       path: 'org_tree_page',
  //       name: 'org_tree_page',
  //       meta: {
  //         icon: 'ios-people',
  //         title: '组织结构树'
  //       },
  //       component: () => import('@/view/example/org-tree')
  //     },
  //     {
  //       path: 'tree_table_page',
  //       name: 'tree_table_page',
  //       meta: {
  //         icon: 'md-git-branch',
  //         title: '树状表格'
  //       },
  //       component: () => import('@/view/example/tree-table/index.vue')
  //     },
  //     {
  //       path: 'cropper_page',
  //       name: 'cropper_page',
  //       meta: {
  //         icon: 'md-crop',
  //         title: '图片裁剪'
  //       },
  //       component: () => import('@/view/example/cropper/cropper.vue')
  //     },
  //     {
  //       path: 'tables_page',
  //       name: 'tables_page',
  //       meta: {
  //         icon: 'md-grid',
  //         title: '行内编辑表格'
  //       },
  //       component: () => import('@/view/example/tables/tables.vue')
  //     },
  //     {
  //       path: 'split_pane_page',
  //       name: 'split_pane_page',
  //       meta: {
  //         icon: 'md-pause',
  //         title: '分割窗口'
  //       },
  //       component: () => import('@/view/example/split-pane/split-pane.vue')
  //     },
  //     {
  //       path: 'icons_page',
  //       name: 'icons_page',
  //       meta: {
  //         icon: '_bear',
  //         title: '自定义图标'
  //       },
  //       component: () => import('@/view/example/icons/icons.vue')
  //     }
  //   ]
  // },
  {
    path: '/401',
    name: 'error_401',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/401.vue')
  },
  {
    path: '/500',
    name: 'error_500',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/500.vue')
  },
  {
    path: '*',
    name: 'error_404',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/404.vue')
  }
]
