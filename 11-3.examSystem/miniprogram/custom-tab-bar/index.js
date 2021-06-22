Component({
  data: {
    color: "#999999",
    selectedColor: "#2C61B4",
    selected: 0,
    list: [{
        pagePath: "/pages/index/index",
        text: "首页",
        iconPath: "../images/icons/index1.png",
        selectedIconPath: "../images/icons/index2.png"
      },
      {
        pagePath: "/pages/course/course",
        text: "学习",
        iconPath: "../images/icons/course1.png",
        selectedIconPath: "../images/icons/course2.png"
      },
      {
        pagePath: "/pages/homepage/homepage",
        text: "我的",
        iconPath: "../images/icons/homepage1.png",
        selectedIconPath: "../images/icons/homepage2.png"
      }
    ]
  },
  attached() {},
  methods: {
    switchTab(e) {
      const data = e.currentTarget.dataset
      const url = data.path
      wx.switchTab({
        url
      })
      this.setData({
        selected: data.index
      })
    }
  }
})